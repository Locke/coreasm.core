/*
 * AgentContext.java 		$Revision: 80 $
 * 
 * Copyright (c) 2009 Roozbeh Farahbod
 *
 * Last modified on $Date: 2009-07-24 16:25:41 +0200 (Fr, 24 Jul 2009) $  by $Author: rfarahbod $
 * 
 * Licensed under the Academic Free License version 3.0 
 *   http://www.opensource.org/licenses/afl-3.0.php
 *   http://www.coreasm.org/afl-3.0.php
 */

package org.coreasm.engine.scheduler;

import java.util.IdentityHashMap;
import java.util.Map;

import org.coreasm.engine.ControlAPI;
import org.coreasm.engine.interpreter.ASTNode;
import org.coreasm.engine.interpreter.Interpreter;
import org.coreasm.engine.interpreter.InterpreterImp;

/**
 * Keeps a context and caching of various info for every Thread.
 *
 */
public class InterpreterCache {

	private static ThreadLocal<InterpreterCache> cache = new ThreadLocal<>();

	public static InterpreterCache get(ControlAPI capi) {
		InterpreterCache result = cache.get();

		if (result == null) {
			Interpreter inter = new InterpreterImp(capi);
			result = new InterpreterCache(inter);
			cache.set(result);
		}

		return result;
	}

	public final Interpreter interpreter;

	private final Map<ASTNode, ASTNode> nodeCopyCache;

	private InterpreterCache(Interpreter interpreter) {
		this.interpreter = interpreter;
		this.nodeCopyCache = new IdentityHashMap<>();
	}

	public ASTNode getCleanCopy(ASTNode node) {
		ASTNode result = nodeCopyCache.get(node);

		if (result == null) {
			result = (ASTNode)this.interpreter.copyTree(node);
			nodeCopyCache.put(node, result);
		}
		else {
			this.interpreter.clearTree(result);
		}

		return result;
	}

	/*
	// TODO: Disposeable??
	@Override
	public void dispose() {
		nodeCopyCache.clear();
	}
	*/
}
