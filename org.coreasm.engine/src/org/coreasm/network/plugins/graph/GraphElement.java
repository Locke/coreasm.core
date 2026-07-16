/*
 * GraphElement.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.jgrapht.Graph;

import org.coreasm.engine.absstorage.Element;

/**
 * Graph elements in CoreASM state.
 *
 * @author Roozbeh Farahbod
 *
 */
public abstract class GraphElement extends Element {

	public static GraphElement createNewInstance() {
		return new DirectedGraphElement();
	}

	@Override
	public String denotation() {
		return "Graph:" + this.toString();
	}

	@Override
	public boolean equals(Object anElement) {
		if (anElement instanceof GraphElement) {
			GraphElement other = (GraphElement) anElement;
			return (this.isDirected() == other.isDirected()) && this.getGraph().equals(other.getGraph());
		} else
			return false;
	}


	@Override
	public int hashCode() {
		return getGraph().hashCode();
	}

	@Override
	public String getBackground() {
		return GraphBackgroundElement.BACKGROUND_NAME;
	}

	@Override
	public String toString() {
		Graph<Element, Element> graph = getGraph();
		return toStringFromSets(graph, graph.vertexSet(), graph.edgeSet(), isDirected());
	}

	/**
	 * Returns a string of the parenthesized pair (V, E) representing this G=(V,E) graph. 'V' is the
	 * string representation of the vertex set, and 'E' is the string representation of the edge
	 * set. The vertex and edge order is sorted by their string representations.
	 *
	 * @param vertexSet the vertex set V to be printed
	 * @param edgeSet the edge set E to be printed
	 * @param directed true to use parens for each edge (representing directed); false to use curly
	 *        braces (representing undirected)
	 *
	 * @return a string representation of (V,E)
	 */
	protected String toStringFromSets(Graph<Element, Element> graph,
			Collection<? extends Element> vertexSet, Collection<? extends Element> edgeSet, boolean directed) {
		List<Element> sortedVertices = new ArrayList<>(vertexSet);
		sortedVertices.sort(Comparator.comparing(String::valueOf));

		List<Element> sortedEdges = new ArrayList<>(edgeSet);
		sortedEdges.sort(Comparator.comparing(String::valueOf));

		List<String> renderedEdges = new ArrayList<>();
		for (Element e : sortedEdges) {
			StringBuilder sb = new StringBuilder();
			sb.append(e);
			sb.append("=");
			if (directed) {
				sb.append("(");
			} else {
				sb.append("{");
			}
			sb.append(graph.getEdgeSource(e));
			sb.append(",");
			sb.append(graph.getEdgeTarget(e));
			if (directed) {
				sb.append(")");
			} else {
				sb.append("}");
			}

			renderedEdges.add(sb.toString());
		}

		return "(" + sortedVertices + ", " + renderedEdges + ")";
	}

	/**
	 * Returns the underlying graph object.
	 */
	public abstract Graph<Element, Element> getGraph();

	/**
	 * @return <code>true</code> if this is a directed graph.
	 */
	public abstract boolean isDirected();
}
