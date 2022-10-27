package org.coreasm.compiler.plugins.extendrule;

import org.coreasm.compiler.CodeType;
import org.coreasm.compiler.CompilerEngine;
import org.coreasm.compiler.exception.CompilationException;
import org.coreasm.compiler.interfaces.CompilerCodePlugin;
import org.coreasm.compiler.interfaces.CompilerPlugin;
import org.coreasm.compiler.plugins.extendrule.code.ucode.ExtendRuleHandler;
import org.coreasm.engine.plugin.Plugin;
import org.coreasm.engine.plugins.extendrule.ExtendRulePlugin;

/**
 * Provides the extend rule.
 * Introduces a new element into an universe
 * and binds it to a local variable in its body
 * @author Spellmaker
 *
 */
public class CompilerExtendRulePlugin extends CompilerCodePlugin implements CompilerPlugin{

	private Plugin interpreterPlugin;

	/**
	 * Constructs a new plugin
	 * @param parent The interpreter version
	 */
	public CompilerExtendRulePlugin(Plugin parent){
		this.interpreterPlugin = parent;
	}

	@Override
	public Plugin getInterpreterPlugin(){
		return interpreterPlugin;
	}

	@Override
	public String getName() {
		return ExtendRulePlugin.PLUGIN_NAME;
	}

	@Override
	public void registerCodeHandlers() throws CompilationException {
		register(new ExtendRuleHandler(), CodeType.U, "Rule", "ExtendRule", null);
	}

	@Override
	public void init(CompilerEngine engine) {
		this.engine = engine;
	}
}
