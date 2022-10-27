package org.coreasm.compiler.plugins.options;

import java.util.Map;

import org.coreasm.compiler.CodeType;
import org.coreasm.compiler.CompilerEngine;
import org.coreasm.compiler.exception.CompilationException;
import org.coreasm.compiler.interfaces.CompilerCodePlugin;
import org.coreasm.compiler.interfaces.CompilerPlugin;
import org.coreasm.compiler.plugins.options.code.bcode.PropertyHandler;
import org.coreasm.engine.plugin.Plugin;
import org.coreasm.engine.plugins.options.OptionsPlugin;

/**
 * Provides functionality to declare options in the specification header
 * @author Spellmaker
 *
 */
public class CompilerOptionsPlugin extends CompilerCodePlugin implements CompilerPlugin {

	private Plugin interpreterPlugin;

	/**
	 * Constructs a new plugin
	 * @param parent The interpreter version
	 */
	public CompilerOptionsPlugin(Plugin parent){
		this.interpreterPlugin = parent;
	}

	@Override
	public void init(CompilerEngine engine) {
		this.engine = engine;
	}

	@Override
	public Plugin getInterpreterPlugin(){
		return interpreterPlugin;
	}
	private Map<String, String> options;

	@Override
	public String getName() {
		return OptionsPlugin.PLUGIN_NAME;
	}

	/**
	 * Provides access to the generated options of the specification
	 * @param name The name of the option
	 * @return The value of the option
	 */
	public String getOption(String name){
		return options.get(name);
	}

	@Override
	public void registerCodeHandlers() throws CompilationException {
		register(new PropertyHandler(), CodeType.BASIC, "Declaration", "PropertyOption", null);
	}
}
