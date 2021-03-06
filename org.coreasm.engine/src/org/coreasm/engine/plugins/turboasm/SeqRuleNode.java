/*	
 * SeqRuleNode.java 	1.0 	$Revision: 243 $
 * 
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
 
package org.coreasm.engine.plugins.turboasm;

import org.coreasm.engine.interpreter.ASTNode;
import org.coreasm.engine.interpreter.ScannerInfo;

/** 
 * A node-wrapper for 'seq' rule nodes.
 *   
 * @author  Roozbeh Farahbod
 * 
 */

public class SeqRuleNode extends ASTNode {

	private static final long serialVersionUID = 1L;

	public SeqRuleNode(String pluginName, String grammarClass, String grammarRule, String token, ScannerInfo scannerInfo) {
		super(pluginName, grammarClass, grammarRule, token, scannerInfo);
	}

	public SeqRuleNode(ScannerInfo info) {
		super(
				TurboASMPlugin.PLUGIN_NAME,
				ASTNode.RULE_CLASS,
				"SeqRule",
				null,
				info);
	}

	public SeqRuleNode(SeqRuleNode node) {
		super(node);
	}
	
	/**
	 * Returns the first rule of the sequence.
	 * 
	 * @return a node
	 */
	public ASTNode getFirstRule() {
		return this.getFirst();
	}
	
	/** 
	 * Returns the second rule of the sequence
	 * 
	 * @return a node
	 */
	public ASTNode getSecondRule() {
		return this.getFirst().getNext();
	}

}
