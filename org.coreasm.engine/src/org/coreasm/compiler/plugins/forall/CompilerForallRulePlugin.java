package org.coreasm.compiler.plugins.forall;

import org.coreasm.compiler.CodeType;
import org.coreasm.compiler.CompilerEngine;
import org.coreasm.compiler.exception.CompilationException;
import org.coreasm.compiler.interfaces.CompilerCodePlugin;
import org.coreasm.compiler.interfaces.CompilerPlugin;
import org.coreasm.compiler.plugins.forall.code.ucode.ForallRuleHandler;
import org.coreasm.engine.plugin.Plugin;
import org.coreasm.engine.plugins.forallrule.ForallRulePlugin;

/**
 * Provides the forall rule
 * @author Spellmaker
 *
 */
public class CompilerForallRulePlugin extends CompilerCodePlugin implements CompilerPlugin{

	private Plugin interpreterPlugin;

	/**
	 * Constructs a new plugin
	 * @param parent The interpreter version
	 */
	public CompilerForallRulePlugin(Plugin parent){
		this.interpreterPlugin = parent;
	}

	@Override
	public Plugin getInterpreterPlugin(){
		return interpreterPlugin;
	}

	@Override
	public String getName() {
		return ForallRulePlugin.PLUGIN_NAME;
	}

	@Override
	public void registerCodeHandlers() throws CompilationException {
		register(new ForallRuleHandler(), CodeType.U, "Rule", "ForallRule", null);
	}

	@Override
	public void init(CompilerEngine engine) {
		this.engine = engine;
	}
}
