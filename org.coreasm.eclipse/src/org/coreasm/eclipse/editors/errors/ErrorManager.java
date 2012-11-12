package org.coreasm.eclipse.editors.errors;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.SortedSet;
import java.util.TreeSet;

import org.codehaus.jparsec.error.ParseErrorDetails;
import org.codehaus.jparsec.error.ParserException;
import org.coreasm.eclipse.editors.ASMDocument;
import org.coreasm.eclipse.editors.ASMEditor;
import org.coreasm.eclipse.editors.ASMParser;
import org.coreasm.eclipse.editors.ASMParser.ParsingResult;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jface.text.BadLocationException;

/**
 * An ErrorManager checks a CoreASM specification for errors beyond
 * syntax errors. Each instance of ASMEditor creates an ErrorManager
 * and binds it to its ASMParser as an Observer. So the ErrorManager
 * gets notified each time the parser was run.
 * 
 * An ErrorManager manages a list of objects implementing the IErrorRecognizer
 * interface. These objects are doing the actual error checking, each error
 * recognizer searches for a certain kind of errors. The ErrorManager executes
 * these ErrorRegognizers after each run of the parser.
 * 
 * @author Markus M�ller
 */
public class ErrorManager implements Observer
{
	private ASMEditor asmEditor;	// the editor this instance belongs to
	private List<ITextErrorRecognizer> listTextParsers;
	private List<ITreeErrorRecognizer> listTreeParsers;
	
	/**
	 * Generates a new ErrorManager and adds all available ErrorRecognizers to itself.
	 * @param asmEditor	The ASMEditor instance the generated ErrorManager belongs to.
	 */
	public ErrorManager(ASMEditor asmEditor)
	{
		this.asmEditor = asmEditor;
		this.listTextParsers = new LinkedList<ITextErrorRecognizer>();
		this.listTreeParsers = new LinkedList<ITreeErrorRecognizer>();

		// Creating and adding all available ErrorRecognizers.
		addErrorParser(new InitErrorRecognizer());
		addErrorParser(new RuleErrorRecognizer());
		addErrorParser(new PluginErrorRecognizer(asmEditor.getParser()));
		addErrorParser(new ModularityErrorRecognizer(asmEditor));
	}
	
	/**
	 * Adds an ErrorRegognizer to this ErrorManager, so the ErrorRecognizer will
	 * be run after each run of the parser.
	 */
	public void addErrorParser(IErrorRecognizer errorParser)
	{
		if (errorParser instanceof ITextErrorRecognizer)
			listTextParsers.add((ITextErrorRecognizer) errorParser);
		
		if (errorParser instanceof ITreeErrorRecognizer)
			listTreeParsers.add((ITreeErrorRecognizer) errorParser);
	}
	
	/**
	 * Executes all ErrorRecognizers implementing the ITextErrorRecognizer interface and
	 * collects the errors which were found in a list.
	 * @param document	The document which is to be checked.
	 * @return			A list with all errors which have been found.
	 * @see				org.coreasm.eclipse.editors.errors.ITextErrorRecognizer
	 */
	public List<AbstractError> checkTextErrorParsers(ASMDocument document)
	{
		List<AbstractError> errors = new LinkedList<AbstractError>();
		for (ITextErrorRecognizer errorParser: listTextParsers)
			errorParser.checkForErrors(document, errors);
		return errors;
	}
	
	/**
	 * Executes all ErrorRecognizers implementing the ITreeErrorRecognizer interface and
	 * collects the errors which were found in a list.
	 * @param document	The document which is to be checked.
	 * @return			A list with all errors which have been found.
	 * @see				org.coreasm.eclipse.editors.errors.ITreeErrorRecognizer
	 */
	public List<AbstractError> checkTreeErrorParsers(ASMDocument document)
	{
		List<AbstractError> errors = new LinkedList<AbstractError>();
		for (ITreeErrorRecognizer errorParser: listTreeParsers)
			errorParser.checkForErrors(document, errors);
		return errors;
	}
	
	/**
	 * Executes all ErrorRecognizers (both TextErrorRecognizers and TreeErrorRecognizers)
	 * interface and collects the errors which were found in a list.
	 * @param document	The document which is to be checked.
	 * @return			A list with all errors which have been found.
	 * @see				org.coreasm.eclipse.editors.errors.ITextErrorRecognizer
	 * @see				org.coreasm.eclipse.editors.errors.ITreeErrorRecognizer
	 */	
	public List<AbstractError> checkAllErrorParsers(ASMDocument document) 
	{
		List<AbstractError> errors = new LinkedList<AbstractError>();
		errors.addAll(checkTextErrorParsers(document));
		errors.addAll(checkTreeErrorParsers(document));
		return errors;
	}

	/**
	 * This is the method of the Observer interface which is called after each
	 * run of the parser. It executes the ErrorRecognizers depending if the parsing
	 * was successful and creates an error marker for each error. It also creates
	 * a marker if the parser delivered a syntax error or an unknown error.
	 * @param o		The observable which has called this method. This must be
	 * 				the parser instance which is bound to the same instance of 
	 * 				ASMEditor than this ErrorManager.
	 * @param arg	The data the Observable delivered. This must be an instance
	 * 				of ParsingResult.
	 */
	@Override
	public void update(Observable o, Object arg)
	{
		// check if correctly called by the right parser
		if (o != asmEditor.getParser() ||
				! (arg instanceof ParsingResult))
			return;
		ParsingResult result = (ParsingResult) arg;
		List<AbstractError> errors = new LinkedList<AbstractError>();
		
		// clear old markers
		asmEditor.removeMarkers(IMarker.PROBLEM);
		
		// always run TextErrorRecognizers
		errors.addAll(checkTextErrorParsers(result.document));
		
		// run TreeErrorRecognizers only if there was no syntax error
		if (result.wasSuccessful == true)
			errors.addAll(checkTreeErrorParsers(result.document));
		
		// create markers for all errors
		for (AbstractError error: errors) {
			if (error instanceof SimpleError)
				asmEditor.createSimpleMark((SimpleError) error, IMarker.SEVERITY_ERROR);
		}

		// if there was a syntax error: create error object
		if (result.exception != null) {
			ParserException pe = result.exception;
			ParseErrorDetails perr = pe.getErrorDetails();
			
			if (perr != null) {
				// SYNTAX ERROR
				int line = pe.getLocation().line;
				int col = pe.getLocation().column;
				int index = 0;
				try {
					index = result.document.getLineOffset(line-1) + col-1;
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
				String message = pe.getMessage();
				String encountered = perr.getEncountered().trim();
				int length = getErrorLength(encountered, result.document.get(), index);
				// build expected string
				List<String> lstExpected = perr.getExpected();
				deleteDuplicatesAndSortList(lstExpected);
				// create error object
				SyntaxError serror = new SyntaxError(message, line, col, index, length, lstExpected, encountered);
				asmEditor.createSyntaxMark(serror, IMarker.SEVERITY_ERROR);
			}
			else
			{	// OTHER ERROR
				String message = pe.getMessage();
				int line = pe.getLocation().line;
				int col = pe.getLocation().column;
				// create error object
				UndefinedError error = new UndefinedError(message, line, col);
				asmEditor.createUndefinedMark(error, IMarker.SEVERITY_ERROR);
			}				
			
		}
		
	}
	
	
	
	// ==============================
	// Helper methods for update(...)
	// ==============================
	
	private int getErrorLength(String token, String strDoc, int index)
	{
		if (token.equals("EOF"))
			return 1;
		if (strDoc.charAt(index)=='"' && strDoc.startsWith(token, index+1))
			return token.length()+2;
		return token.length();
	}
	
	private void deleteDuplicatesAndSortList(List<String> list)
	{
		SortedSet<String> setEntries = new TreeSet<String>();
		for (String entry: list)
			if ( ! setEntries.contains(entry) )
				setEntries.add(entry);
		list.clear();
		list.addAll(setEntries);
	}
	
}