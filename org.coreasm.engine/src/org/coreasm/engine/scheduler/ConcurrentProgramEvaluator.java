/*
 * ConcurrentProgramEvaluator.java 		$Revision: 80 $
 * 
 * Copyright (c) 2008 Roozbeh Farahbod
 *
 * Last modified on $Date: 2009-07-24 16:25:41 +0200 (Fr, 24 Jul 2009) $  by $Author: rfarahbod $
 * 
 * Licensed under the Academic Free License version 3.0 
 *   http://www.opensource.org/licenses/afl-3.0.php
 *   http://www.coreasm.org/afl-3.0.php
 *
 */


package org.coreasm.engine.scheduler;

import java.util.Set;
import java.util.concurrent.RecursiveTask;

import org.coreasm.engine.ControlAPI;
import org.coreasm.engine.EngineException;
import org.coreasm.engine.absstorage.*;
import org.coreasm.engine.interpreter.ASTNode;
import org.coreasm.engine.interpreter.Interpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Evaluates programs of a set of agents in parallel using
 * Java concurrency methods.
 *   
 * @author Roozbeh Farahbod
 *
 */

public class ConcurrentProgramEvaluator extends RecursiveTask<UpdateMultiset> {

	protected static final Logger logger = LoggerFactory.getLogger(ConcurrentProgramEvaluator.class);
	
	private final ControlAPI capi;
	private final AbstractStorage storage;
	private final Element agent;
	private Set<Update> injectUpdates = null;

	private final boolean shouldPrintExecutionStats;
	private String executionStats;

	/**
	 * Creates a new program evaluator.
	 * 
	 * @param capi
	 * @param agent
	 */
	public ConcurrentProgramEvaluator(ControlAPI capi, Element agent, boolean shouldPrintExecutionStats) {
		this.agent = agent;
		this.capi = capi;
		this.storage = capi.getStorage();
		this.shouldPrintExecutionStats = shouldPrintExecutionStats;
	}

	/**
	 * Creates a new program evaluator.
	 *
	 * @param capi
	 * @param agent
	 * @param injectUpdates
	 */
	public ConcurrentProgramEvaluator(ControlAPI capi, Element agent, Set<Update> injectUpdates, boolean shouldPrintExecutionStats) {
		this(capi, agent, shouldPrintExecutionStats);
		this.injectUpdates = injectUpdates;
	}

	@Override
	public UpdateMultiset compute() {
		long startTime = System.nanoTime();

		UpdateMultiset result;
		try {
			result = evaluate(agent);
		} catch(EngineException e) {
			throw new RuntimeException("could not evaluate agent", e);
		}

		if (shouldPrintExecutionStats) {
			executionStats = Thread.currentThread().toString() +
					" took " +
					(System.nanoTime() - startTime) / 1e6 +
					"ms to evaluate agent " + agent;
		}

		return result;
	}
	
	/*
	 * Evaluates the program of the given agent.
	 */
	private UpdateMultiset evaluate(Element agent) throws EngineException {
		if (this.injectUpdates != null) {
			storage.pushState("ProgramEvaluatorTask");
			storage.apply(this.injectUpdates);
			logger.info("applying injected Updates: " + this.injectUpdates);
		}

		final InterpreterCache context = InterpreterCache.get(capi);

		final Interpreter inter = context.interpreter;
		inter.cleanUp(); // FIXME: disable for now; there might be dragons around here on recursion..

		Element program = storage.getChosenProgram(agent);
		if (program.equals(Element.UNDEF)) 
			throw new EngineException("Program of agent " + agent.denotation() + " is undefined.");
		if (!(program instanceof RuleElement)) 
			throw new EngineException("Program of agent " + agent.denotation() + " is not a rule element: " + program);
		inter.setSelf(agent);
		
		ASTNode ruleNode = ((RuleElement)program).getBody();
		ASTNode rootNode = context.getCleanCopy(ruleNode);
		
		inter.setPosition(rootNode);
		// allow the interpreter to perform internal initialization 
		// prior to program execution
		inter.initProgramExecution();

		do 
			inter.executeTree();	
		while (!(inter.isExecutionComplete() || capi.hasErrorOccurred()));
		
		// if rootNode hasn't been evaluated after inter.isExecutionComplete() returned true, the AST has been corrupted
		if (!rootNode.isEvaluated() && !capi.hasErrorOccurred()) 
			throw new EngineException("AST of " + agent.denotation() + program.denotation() + " has been corrupted.");
		
		// if an error occurred in the engine, just return an empty multiset
		UpdateMultiset result;
		if (capi.hasErrorOccurred()) 
			result = new UpdateMultiset();
		else
			result = rootNode.getUpdates();

		if (logger.isDebugEnabled())
			logger.info("Updates are: " + result.toString());

		if (this.injectUpdates != null && !capi.hasErrorOccurred()) {
			// automatically discard the injected updates, and returns only the updates computed from this agent
			storage.popState("ProgramEvaluatorTask");
		}

		if (logger.isDebugEnabled())
			logger.info("composed updates are: " + result.toString());

		return result;
	}

	public String getExecutionStats() {
		return executionStats;
	}
	
}
