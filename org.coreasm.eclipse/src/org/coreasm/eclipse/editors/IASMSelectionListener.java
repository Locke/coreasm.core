package org.coreasm.eclipse.editors;

import org.eclipse.jface.text.ITextSelection;

import org.coreasm.engine.interpreter.ASTNode;

public interface IASMSelectionListener {
	public void selectionChanged(ASMEditor editor, ITextSelection selection, ASTNode root);
}
