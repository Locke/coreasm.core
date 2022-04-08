/*
 * TimePlugin.java 	1.0 	$Revision: 243 $
 *
 * Copyright (C) 2006 Roozbeh Farahbod
 *
 * Last modified by $Author: rfarahbod $ on $Date: 2011-03-29 02:05:21 +0200 (Di, 29 Mrz 2011) $.
 *
 * Licensed under the Academic Free License version 3.0
 *   http://www.opensource.org/licenses/afl-3.0.php
 *   http://www.coreasm.org/afl-3.0.php
 *
 */

package org.coreasm.engine.plugins.uuid;

import org.coreasm.engine.VersionInfo;
import org.coreasm.engine.absstorage.BackgroundElement;
import org.coreasm.engine.absstorage.FunctionElement;
import org.coreasm.engine.absstorage.RuleElement;
import org.coreasm.engine.absstorage.UniverseElement;
import org.coreasm.engine.plugin.Plugin;
import org.coreasm.engine.plugin.VocabularyExtender;
import org.coreasm.engine.plugins.string.StringBackgroundElement;

import java.util.*;

/**
 * Provides UUID-related functions.
 *
 * @author  André Wolski
 *
 */
public class UUIDPlugin extends Plugin implements VocabularyExtender {

	public static final VersionInfo VERSION_INFO = new VersionInfo(0, 1, 0, "");

	private final Set<String> dependencyList = Collections.emptySet();

	private Map<String, FunctionElement> functions = null;
	private Map<String, BackgroundElement> backgroundElements = null;

	/**
	 *
	 */
	public UUIDPlugin() {
		super();
	}

	/**
	 * Creates necessary functions.
	 *
	 * @see Plugin#initialize()
	 */
	@Override
	public void initialize() {
	}

	/**
	 * Returns the list of functions provided by this plugin.
	 */
	@Override
	public Map<String,FunctionElement> getFunctions() {
		if (functions == null) {
			functions = new HashMap<>();
			functions.put(NilUUIDFunctionElement.NIL_UUID_FUNC_NAME, new NilUUIDFunctionElement());
			functions.put(RandomUUIDFunctionElement.RANDOM_UUID_FUNC_NAME, new RandomUUIDFunctionElement());
		}
		return functions;
	}

	/**
	 * @return <code>null</code>
	 */
	@Override
	public Map<String,UniverseElement> getUniverses() {
		return Collections.emptyMap();
	}

	@Override
	public Set<String> getRuleNames() {
		return Collections.emptySet();
	}

	@Override
	public Map<String, RuleElement> getRules() {
		return null;
	}

	/**
	 * @return <code>null</code>
	 */
	@Override
	public Map<String,BackgroundElement> getBackgrounds() {
		if (backgroundElements == null) {
			backgroundElements = new HashMap<>();
			BackgroundElement uuidBackgroundElement = new UUIDBackgroundElement();
			backgroundElements.put(UUIDBackgroundElement.UUID_BACKGROUND_NAME, uuidBackgroundElement);
		}
		return backgroundElements;
	}

	/* (non-Javadoc)
	 * @see org.coreasm.engine.plugin.Plugin#getDependencyNames()
	 */
	@Override
	public Set<String> getDependencyNames() {
		return this.dependencyList;
	}

	@Override
	public Set<String> getBackgroundNames() {
		return backgroundElements.keySet();
	}

	@Override
	public Set<String> getFunctionNames() {
		return getFunctions().keySet();
	}

	@Override
	public Set<String> getUniverseNames() {
		return Collections.emptySet();
	}

	@Override
	public VersionInfo getVersionInfo() {
		return VERSION_INFO;
	}


}
