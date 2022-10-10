package CompilerRuntime;

public class CoreASMError extends RuntimeException {

	// v1 -> v2: extends Error -> extends RuntimeException
	private static final long serialVersionUID = 2L;

	public CoreASMError(String s){
		super(s);
	}

	public CoreASMError(String s, ASTNode n){
		super(s);
	}
}
