/*
 * CycleDetectorCache.java
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

import java.util.HashMap;

import org.jgrapht.Graph;
import org.jgrapht.alg.cycle.CycleDetector;

import org.coreasm.engine.absstorage.Element;

/**
 * A cache of cycle detectors for graphs.
 *
 * @author Roozbeh Farahbod
 *
 */
public class CycleDetectorCache {

	private final HashMap<Graph<Element,Element>, CycleDetector<Element,Element>> detectorCache = new HashMap<>();

	/**
	 * Returns a cycle detector for the given graph g, assuming that g does not change.
	 *
	 * @param g an instance of {@link Graph}
	 */
	public CycleDetector<Element, Element> getCycleDetector(Graph<Element, Element> g) {
		return detectorCache.computeIfAbsent(g, CycleDetector::new);
	}

}
