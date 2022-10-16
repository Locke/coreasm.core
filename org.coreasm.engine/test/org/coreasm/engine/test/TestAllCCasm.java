package org.coreasm.engine.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the CoreASM compiler
 * @author Spellmaker
 *
 */
public class TestAllCCasm {
	//streams for in and output
	private final ByteArrayOutputStream logContent = new ByteArrayOutputStream();
	//private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	final static PrintStream origOutput = System.out;
	final static PrintStream origError = System.err;
	//list of test cases
	protected static List<File> testFiles = null;

	@BeforeClass
	public static void onlyOnce() {
		//setup the test by finding the test specifications
		URL url = TestAllCasm.class.getClassLoader().getResource("./without_test_class");

		try {
			testFiles = new LinkedList<File>();
			//recursively search for specifications
			TestAllCasm.addTestFiles(testFiles, new File(url.toURI()));
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Before
	public void setUpStreams() {
		//redirect output
		System.setOut(new PrintStream(logContent));
		System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
		//reset in and output to defaults
		System.setOut(origOutput);
		System.setErr(origError);
	}

	@Test
	public void performTest(){
		boolean successful = true;
		//check if there are files for testing for this class
		if (testFiles.isEmpty()) {
			TestReport t = new TestReport(null, "no test file found!", -1, false);
			successful = false;
		}
		//perform test for all test files, output result, and modify test result if test has failed
		for (File testFile : testFiles) {
			TestReport t = CompilerDriver.runSpecification(testFile);
			t.print(origOutput, origError);
			if (!t.successful()) {
				successful = false;
				if (TestUtils.failFast) break;
			}
		}
		//report overall test result
		//test failed if at least one test has failed
		if (!successful)
			Assert.fail("Test failed for class: " + TestAllCCasm.class.getSimpleName());
	}

	protected static void addTestFile(List<File> testFiles, File file, Class<?> clazz) {
		TestUtils.addCompilerTestFile(testFiles, file, clazz);
	}

}
