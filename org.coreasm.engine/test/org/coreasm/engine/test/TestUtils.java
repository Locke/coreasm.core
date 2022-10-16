package org.coreasm.engine.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.coreasm.util.Tools;

public class TestUtils {

	protected static final boolean failFast;

	static {
		failFast = Boolean.getBoolean("TestUtils.failFast");
	}

	public static List<String> getFilteredOutput(File file, String filter) {
		List<String> filteredOutputList = new LinkedList<String>();
		Pattern pattern = Pattern.compile(filter + ".*");
		try (FileReader fileReader = new FileReader(file);
		     BufferedReader input = new BufferedReader(fileReader)) {
			String line; //not declared within while loop
			while ((line = input.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					int first = line.indexOf("\"", matcher.start()) + 1;
					int last = line.indexOf("\"", first);
					if (last > first)
						filteredOutputList.add(Tools.convertFromEscapeSequence(line.substring(first, last)));
				}
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return filteredOutputList;
	}

	public static int getParameter(File file, String name) {
		int value = -1;
		Pattern pattern = Pattern.compile("@" + name + "\\s*(\\d+)");
		try (FileReader fileReader = new FileReader(file);
		     BufferedReader input = new BufferedReader(fileReader)) {
			String line;
			while ((line = input.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					value = Integer.parseInt(matcher.group(1));
					break;
				}
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static void addTestFile(List<File> testFiles, File file, Class<?> clazz) {
		if (!testFiles.isEmpty())
			return;
		if (file != null && file.isDirectory())
			for (File child : file.listFiles(new FileFilter() {

				@Override
				public boolean accept(File file) {
					return (file.isDirectory()
							|| file.getName().toLowerCase().endsWith(".casm")
							|| file.getName().toLowerCase().endsWith(".coreasm"));
				}
			})) {
				addTestFile(testFiles, child, clazz);
			}
		else if (file != null
				&& file.getName().toLowerCase().matches(clazz.getSimpleName().toLowerCase() + "(.casm|.coreasm)"))
			testFiles.add(file);
	}

	public static void addCompilerTestFile(List<File> testFiles, File file, Class<?> clazz) {
		if (!testFiles.isEmpty())
			return;
		if (file != null && file.isDirectory())
			for (File child : file.listFiles(new FileFilter() {

				@Override
				public boolean accept(File file) {
					return (file.isDirectory()
							|| file.getName().toLowerCase().endsWith(".casm")
							|| file.getName().toLowerCase().endsWith(".coreasm"));
				}
			})) {
				addCompilerTestFile(testFiles, child, clazz);
			}
		else if (file != null
				&& file.getName().toLowerCase().matches(clazz.getSimpleName().replace("Compiler", "").toLowerCase() + "(.casm|.coreasm)"))
			testFiles.add(file);
	}

	public static void addTestFiles(List<File> testFiles, File file) {
		if (file != null && file.isDirectory())
			for (File child : file.listFiles(new FileFilter() {

				@Override
				public boolean accept(File file) {
					return (file.isDirectory()
							|| file.getName().toLowerCase().endsWith(".casm")
							|| file.getName().toLowerCase().endsWith(".coreasm"));
				}
			})) {
				addTestFiles(testFiles, child);
			}
		else if (file != null)
			testFiles.add(file);
	}

}
