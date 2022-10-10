package org.coreasm.compiler.exception;

/**
 * Parent of all CoreASM Compiler Exceptions
 */
public class CoreASMCompilerException extends Exception {

	public CoreASMCompilerException() {
		super();
	}

	public CoreASMCompilerException(String message) {
		super(message);
	}

	public CoreASMCompilerException(Exception cause) {
		super(cause);
	}
}
