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

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.RecursiveTask;

import org.coreasm.engine.ControlAPI;
import org.coreasm.engine.EngineException;
import org.coreasm.engine.absstorage.AbstractStorage;
import org.coreasm.engine.absstorage.Element;
import org.coreasm.engine.absstorage.RuleElement;
import org.coreasm.engine.absstorage.UpdateMultiset;
import org.coreasm.engine.interpreter.ASTNode;
import org.coreasm.engine.interpreter.Interpreter;
import org.coreasm.engine.interpreter.InterpreterImp;
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

	public static final int DEFAULT_BATCH_SIZE = 1;

	protected static final Logger logger = LoggerFactory.getLogger(ConcurrentProgramEvaluator.class);

	public final AgentContextMap agentContextMap;
	
	private final ControlAPI capi;
	private final AbstractStorage storage;
	private List<? extends Element> agents = null;
	private final int start;
	private final int end;
	private final int batchSize;
	private final boolean shouldPrintExecutionStats;
	private StringBuilder executionStats;
	
	/**
	 * Creates a new program evaluator working on agents [start, ..., end-1] in the list.
	 * 
	 * @param capi
	 * @param agents
	 * @param start
	 * @param end
	 */
	public ConcurrentProgramEvaluator(ControlAPI capi, AgentContextMap agentContextMap, List<? extends Element> agents, int start, int end, boolean shouldPrintExecutionStats) {
		this(capi, agentContextMap, agents, start, end, DEFAULT_BATCH_SIZE, shouldPrintExecutionStats);
	}
	
	/**
	 * Creates a new program evaluator working on agents [start, ..., end-1] in the list.
	 * 
	 * @param capi
	 * @param agents
	 * @param start
	 * @param end
	 */
	public ConcurrentProgramEvaluator(ControlAPI capi, AgentContextMap agentContextMap, List<? extends Element> agents,  int start, int end, int batchSize, boolean shouldPrintExecutionStats) {
		this.agents = agents;
		this.capi = capi;
		this.storage = capi.getStorage();
		this.start = start;
		this.end = end;
		this.batchSize = batchSize;
		this.agentContextMap = agentContextMap;
		this.shouldPrintExecutionStats = shouldPrintExecutionStats;
	}

	@Override
	public UpdateMultiset compute() {
		if (end - start > batchSize) {
			int cut = start + (end - start) / 2;
			ConcurrentProgramEvaluator cpe1 = new ConcurrentProgramEvaluator(capi, agentContextMap, agents, start, cut, shouldPrintExecutionStats);
			ConcurrentProgramEvaluator cpe2 = new ConcurrentProgramEvaluator(capi, agentContextMap, agents, cut, end, shouldPrintExecutionStats);

			cpe2.fork();
			UpdateMultiset result1 = cpe1.invoke();

			if (result1 == null) {
				cpe2.cancel(true);
				return null;
			} else {
				UpdateMultiset result2 = cpe2.join();

				if (result2 == null) {
					return null;
				}

				if (shouldPrintExecutionStats) {
					executionStats = new StringBuilder(cpe1.executionStats).append(cpe2.executionStats);
				}

				UpdateMultiset result = new UpdateMultiset(result1);
				result.addAll(result2);
				return result;
			}
		} else {
			long startTime = System.nanoTime();

			UpdateMultiset aggregatedResult = new UpdateMultiset();
			List<? extends Element> myAgents = agents.subList(start, end);
			for (Element agent : myAgents) {
				UpdateMultiset result;
				try {
					result = evaluate(agent);
				} catch(EngineException e) {
					throw new RuntimeException("could not evaluate agent", e);
				}
				aggregatedResult.addAll(result);
			}

			if (shouldPrintExecutionStats) {
				executionStats = new StringBuilder()
						.append(Thread.currentThread().toString())
						.append(" took ")
						.append((System.nanoTime() - startTime) / 1e6)
						.append("ms to evaluate ")
						.append(end - start)
						.append(" agent(s): ")
						.append(Arrays.toString(myAgents.toArray()))
						.append("\n");
			}

			return aggregatedResult;
		}
	}
	
	/*
	 * Evaluates the program of the given agent.
	 */
	private UpdateMultiset evaluate(Element agent) throws EngineException {
		AgentContext context = agentContextMap.get(agent); 
		Interpreter inter;
		if (context == null) {
			context = new AgentContext(agent);
			agentContextMap.put(agent, context);
			context.interpreter = new InterpreterImp(capi);
			inter = context.interpreter;
		} else {
			inter = context.interpreter;
			inter.cleanUp();
		}
		inter.cleanUp();

		Element program = storage.getChosenProgram(agent);
		if (program.equals(Element.UNDEF)) 
			throw new EngineException("Program of agent " + agent.denotation() + " is undefined.");
		if (!(program instanceof RuleElement)) 
			throw new EngineException("Program of agent " + agent.denotation() + " is not a rule element.");
		inter.setSelf(agent);
		
		ASTNode ruleNode = ((RuleElement)program).getBody();
		ASTNode rootNode = context.nodeCopyCache.get(ruleNode);
		if (rootNode == null) {
			rootNode = (ASTNode)inter.copyTree(ruleNode); 
			context.nodeCopyCache.put(ruleNode, rootNode);
		} else {
			inter.clearTree(rootNode);
		}
		
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
			logger.debug("Updates are: " + result.toString());

		return result;
	}

	public String getExecutionStats() {
		return executionStats.toString();
	}
	
}
