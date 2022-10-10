package org.coreasm.compiler.plugins.math.code.rcode;

import org.coreasm.compiler.CompilerEngine;
import org.coreasm.compiler.codefragment.CodeFragment;
import org.coreasm.compiler.exception.CompilationException;
import org.coreasm.compiler.interfaces.CompilerCodeHandler;
import org.coreasm.engine.interpreter.ASTNode;

/**
 * Handles the generation of random values
 * @author Spellmaker
 *
 */
public class RandomValueHandler implements CompilerCodeHandler {

	@Override
	public void compile(CodeFragment result, ASTNode node, CompilerEngine engine)
			throws CompilationException {
		result.appendLine("evalStack.push(@NumberElement@(Math.random()));\n");
	}

}
