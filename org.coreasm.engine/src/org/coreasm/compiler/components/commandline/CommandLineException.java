package org.coreasm.compiler.components.commandline;

import org.coreasm.compiler.exception.CoreASMCompilerException;

/**
 * Thrown to signalize an error in the command line
 * of CoreASMC
 * @author Markus Brenner
 *
 */
@Deprecated
public class CommandLineException extends CoreASMCompilerException {
	/**
	 * @param string The concrete error message
	 */
	public CommandLineException(String string) {
		super(string);
	}

	/**
	 * Default serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

}
