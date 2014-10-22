package org.coreasm.compiler.classlibrary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.coreasm.compiler.CompilerOptions;
import org.coreasm.compiler.CoreASMCompiler;
import org.coreasm.compiler.classlibrary.ClassFile;
import org.coreasm.compiler.classlibrary.ClassInclude;
import org.coreasm.compiler.classlibrary.ClassLibrary;
import org.coreasm.compiler.classlibrary.LibraryEntry;
import org.coreasm.compiler.exception.EntryAlreadyExistsException;
import org.coreasm.compiler.exception.IncludeException;
import org.coreasm.compiler.exception.LibraryEntryException;
import org.coreasm.compiler.interfaces.CompilerPlugin;

/**
 * Manages code units generated by the compiler and holds them temporarily
 * until the compiler commands it to write all files to the hard drive.
 * It also provides methods for the inclusion of already existing java code files
 * on the hard drive or from a jar archive
 * @author Markus Brenner
 *
 */
public class ClassLibrary {
	/**
	 * Base package all plugin generated objects will reside in.
	 * The full package path for a class clazz created by plugin foo
	 * will be PLUGINBASEPACKAGE.foo.clazz
	 */
	public static final String PLUGINBASEPACKAGE = "plugins";
	
	private ArrayList<LibraryEntry> entries;
	private CompilerOptions options;
	private Map<String, String> packageReplacements;
	
	/**
	 * constructs an empty class library with the given options
	 * @param options An option object, containing the parameters
	 * for this library, mainly paths to the temporary directory
	 */
	public ClassLibrary(CompilerOptions options){
		entries = new ArrayList<LibraryEntry>();
		this.options = options;
		packageReplacements = new HashMap<String, String>();
	}
	
	/**
	 * Adds a library entry to this class library
	 * @param cf The new library entry
	 * @throws EntryAlreadyExistsException If an entry with the given name already exists within the library.
	 * The plugin developer should carefully choose his names, so that this exception is never thrown.
	 * Note that a name is only considered equal, if a file would overwrite an already existing file,
	 */
	public void addEntry(LibraryEntry cf) throws EntryAlreadyExistsException{
		if(!entries.contains(cf)){
			this.entries.add(cf);
		}
		else{
			CoreASMCompiler.getEngine().addError("an entry with name " + cf.getFullName() + " already exists");
			throw new EntryAlreadyExistsException("Class already exists");
		}
	}
	
	/**
	 * Builds the class name for the given path, if included by the given plugin. 
	 * This can be used to check names when generating
	 * code which uses classes provided by other plugins.
	 * @param name The name of the class
	 * @param classSource The plugin source. If null, the class will be
	 * assumed to reside in PLUGINBASEPACKAGE
	 * @return The full name of the class in the generated code
	 */
	public String getClassName(String name, CompilerPlugin classSource){
		if(classSource == null) return name;
		return PLUGINBASEPACKAGE + "." + classSource.getName() + "." + name;
	}
	
	/**
	 * Searches for an already existing library entry with the given full name.
	 * @param fullName The full name (path) of the library entry 
	 * @return A library entry with the given full name or null, if the entry was not found
	 */
	public LibraryEntry findEntry(String fullName){
		CoreASMCompiler.getEngine().getLogger().debug(ClassLibrary.class, "searching for entry \"" + fullName + "\"");
		for(LibraryEntry le : entries){
			if(le.getFullName().equals(fullName)) return le;
		}
		CoreASMCompiler.getEngine().getLogger().debug(ClassLibrary.class, "could not find requested library entry " + fullName + " in the class library");
		return null;
	}
	
	/*
	 * Note: The following functions are actually a bit redundant / useless, as all actions can also
	 * be performed by generating a correct library entry and adding it with addEntry.
	 * Only reason to keep them alive is to help the programmer with the creation of correct full names,
	 * so that all entries land in the correct package
	 */
	
	/**
	 * Creates a new empty class with the given name and the given plugin source. 
	 * If soure is null, the class will reside in PLUGINBASEPACKAGE. Otherwise,
	 * the class resides in PLUGINBASEPACKAGE . source 
	 * @param name The name of the class
	 * @param source The name of the plugin creating this class object
	 * @return The new ClassFile object.
	 * @throws EntryAlreadyExistsException if an entry with the constructed full name already exists
	 */
	public ClassFile createClass(String name, CompilerPlugin source) throws EntryAlreadyExistsException{
		ClassFile cf = null;
		if(source == null) 	cf = new ClassFile(name, "");
		else 				cf = new ClassFile(name, PLUGINBASEPACKAGE + "." + source.getName());
		
		if(!this.entries.contains(cf)){
			this.entries.add(cf);
			return cf;
		}
		throw new EntryAlreadyExistsException(cf.getFullName());
	}
	
	/**
	 * Includes an already existing java class file from the hard drive into the class library.
	 * @param path The path to the existing file, also the source for the name of the file
	 * @param source The plugin source, the name will be used to generate the package path
	 * @return A new ClassInclude LibraryEntry for the included class.
	 * @throws EntryAlreadyExistsException If there already is an entry with the given full name
	 */
	public ClassInclude includeClass(String path, CompilerPlugin source) throws EntryAlreadyExistsException{
		ClassInclude result = new ClassInclude(path, PLUGINBASEPACKAGE + "." + source.getName());
		if(this.entries.contains(result)) throw new EntryAlreadyExistsException(result.getFullName());
		this.entries.add(result);
		return result;
	}
	
	/**
	 * Includes an already existing java class file from a jar archive into the class library.
	 * @param jarPath The path of the jar archive
	 * @param classPath The path of the class in the jar archive
	 * @param source The plugin source, the name will be used to generate the package path
	 * @return A new ClassInclude LibraryEntry for the included class
	 * @throws IncludeException If there was some problem with the jar archive 
	 * @throws EntryAlreadyExistsException If an entry with the constructed full name already exists
	 */
	public ClassInclude includeClass(String jarPath, String classPath, CompilerPlugin source) throws IncludeException, EntryAlreadyExistsException{
		try{
			ClassInclude result = new ClassInclude(jarPath, classPath, PLUGINBASEPACKAGE + "." + source.getName());
			if(this.entries.contains(result)) throw new EntryAlreadyExistsException(result.getFullName());
			this.entries.add(result);
			return result;
		}
		catch(IOException e){
			CoreASMCompiler.getEngine().addError("jar file " + jarPath + " could not be accessed");
			CoreASMCompiler.getEngine().getLogger().error(ClassLibrary.class, "Could not add file");
			throw new IncludeException("could not add file from jar " + jarPath + " " + classPath);
		}
	}
	
	/**
	 * Adds a global package replacement entry.
	 * This will order all ClassIncludes to replace the given package declaration with
	 * the given string
	 * @param original The original package path
	 * @param replace The replacement package path
	 */
	public void addPackageReplacement(String original, String replace){
		this.packageReplacements.put(original, replace);
	}
	
	/**
	 * Writes all stored classes / entries to the temporary directory specified by the CompilerOption object
	 * @return A list of generated files with their full paths. This can be used to compile the generated classes
	 * @throws LibraryEntryException If a LibraryEntry failed to write its contents to the disk
	 */
	public ArrayList<String> dumpClasses() throws LibraryEntryException{
		CoreASMCompiler.getEngine().getLogger().debug(ClassLibrary.class, "starting class dump, library contains the following classes:");
		//debug output: list all files in the class library
		for(LibraryEntry e : entries){
			CoreASMCompiler.getEngine().getLogger().debug(ClassLibrary.class, e.getFullName());
		}
		ArrayList<String> result = new ArrayList<String>();
		
		boolean hadError = false;
		
		//process all library entries
		for(int i = 0; i < entries.size(); i++){
			LibraryEntry e = entries.get(i);
			
			//add package replacements to class includes
			if(e instanceof ClassInclude){
				ClassInclude tmp = (ClassInclude) e;
				for(Entry<String, String> entry : packageReplacements.entrySet()){
					tmp.addImportReplacement(entry.getKey(), entry.getValue());
				}
			}
			
			CoreASMCompiler.getEngine().getLogger().debug(ClassLibrary.class, "current entry: " + e.toString());
			CoreASMCompiler.getEngine().getLogger().debug(ClassLibrary.class, "dumping class " + options.tempDirectory + "\\" + e.getFullName().replace(".", "\\") + ".java");
			//write the library Entry
			try{
				e.writeFile();
				CoreASMCompiler.getEngine().getLogger().debug(ClassLibrary.class, "success");
			}
			catch(LibraryEntryException exc){
				CoreASMCompiler.getEngine().addError("entry " + options.tempDirectory + "\\" + e.getFullName().replace(".", "\\") + ".java" + " had errors");
				hadError = true;
			}
			
			//add the printed file to the list of generated files
			result.add(options.tempDirectory + "\\" + e.getFullName().replace(".", "\\") + ".java");
		}
		
		if(hadError) throw new LibraryEntryException("");
		
		return result;
	}
}
