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

	private static final FileFilter directoryOrCoreasmFileFilter = file -> (file.isDirectory()
			|| file.getName().toLowerCase().endsWith(".casm")
			|| file.getName().toLowerCase().endsWith(".coreasm"));

	public static List<String> getFilteredOutput(File file, String filter) {
		List<String> filteredOutputList = new LinkedList<>();
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
		return List.copyOf(filteredOutputList);
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

	public static TestCase parseTestCase(File testFile) {
		// extract parameters and expected results from the testcase
		List<String> requiredOutputList = TestUtils.getFilteredOutput(testFile, "@require");
		List<String> refusedOutputList = TestUtils.getFilteredOutput(testFile, "@refuse");
		int minSteps = TestUtils.getParameter(testFile, "minsteps");
		if (minSteps <= 0)
			minSteps = 1;
		int maxSteps = TestUtils.getParameter(testFile, "maxsteps");
		if (maxSteps < minSteps)
			maxSteps = minSteps;

		return new TestCase(testFile, requiredOutputList, refusedOutputList, minSteps, maxSteps);
	}

	public static DetailedTestCase parseDetailedTestCase(File testFile) {
		TestCase base = parseTestCase(testFile);

		List<DetailedTestCase.TestCaseStep> testCaseSteps = getDetailedSteps(testFile);

		return new DetailedTestCase(base, testCaseSteps);
	}

	public static List<DetailedTestCase.TestCaseStep> getDetailedSteps(File file) {
		List<DetailedTestCase.TestCaseStep> detailedSteps = new LinkedList<>();
		Pattern pattern = Pattern.compile("@(do|check).*");
		try (FileReader fileReader = new FileReader(file);
			 BufferedReader input = new BufferedReader(fileReader)) {
			String line; //not declared within while loop
			while ((line = input.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					String cleanLine = line.substring(matcher.start()); // remove beginning comment sequence and whitespaces
					DetailedTestCase.TestCaseStep detailedStep = DetailedTestCase.TestCaseStep.parse(cleanLine);
					detailedSteps.add(detailedStep);
				}
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return List.copyOf(detailedSteps);
	}

	public static void addTestFile(List<File> testFiles, File file, Class<?> clazz) {
		addNamedFile(testFiles, file, clazz.getSimpleName().toLowerCase() + "(.casm|.coreasm)");
	}

	public static void addCompilerTestFile(List<File> testFiles, File file, Class<?> clazz) {
		addNamedFile(testFiles, file, clazz.getSimpleName().replace("Compiler", "").toLowerCase() + "(.casm|.coreasm)");
	}

	private static void addNamedFile(List<File> testFiles, File file, String lowercaseRegex) {
		if (!testFiles.isEmpty())
			return;
		if (file != null && file.isDirectory())
			for (File child : file.listFiles(directoryOrCoreasmFileFilter)) {
				addNamedFile(testFiles, child, lowercaseRegex);
			}
		else if (file != null
				&& file.getName().toLowerCase().matches(lowercaseRegex))
			testFiles.add(file);
	}

	public static void addTestFiles(List<File> testFiles, File file) {
		if (file != null && file.isDirectory())
			for (File child : file.listFiles(directoryOrCoreasmFileFilter)) {
				addTestFiles(testFiles, child);
			}
		else if (file != null)
			testFiles.add(file);
	}

}
