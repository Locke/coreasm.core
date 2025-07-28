/*
 * DirectedGraphElement.java
 *
 * Copyright (C) 2010 Roozbeh Farahbod
 *
 * Last modified by $Author$ on $Date$.
 *
 * Licensed under the Academic Free License version 3.0
 *   http://www.opensource.org/licenses/afl-3.0.php
 *   http://www.coreasm.org/afl-3.0.php
 *
 */
package org.coreasm.network.plugins.graph;

import org.jgrapht.Graph;

import org.coreasm.engine.absstorage.Element;

/**
 * Directed graph elements in CoreASM state.
 *
 * @author Roozbeh Farahbod
 *
 */
public class DirectedGraphElement extends GraphElement {

	protected final Graph<Element, Element> graph;

	/**
	 * Creates a new graph element based on the given graph.
	 */
	protected DirectedGraphElement(Graph<Element, Element> graph) {
		this.graph = graph;
	}

	/**
	 * Creates a new directed graph.
	 */
	public DirectedGraphElement() {
		this(GraphPlugin.createDefaultGraph());
	}

	@Override
	public Graph<Element, Element> getGraph() {
		return graph;
	}

	@Override
	public boolean isDirected() {
		return true;
	}
}
