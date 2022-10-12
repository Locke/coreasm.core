package org.coreasm.engine.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.coreasm.engine.Engine;
import org.coreasm.engine.EngineProperties;
import org.coreasm.util.Tools;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestAllCasm {

	protected static List<File> testFiles = null;

	private static final java.util.Properties properties;

	static {
		java.util.Properties props = new EngineProperties();
		props.setProperty(EngineProperties.PRINT_STACK_TRACE, EngineProperties.YES);
		properties = props;
	}

	@BeforeClass
	public static void onlyOnce() {
		//setup the test by finding the test specifications
		URL url = TestAllCasm.class.getClassLoader().getResource("./without_test_class");

		try {
			testFiles = new LinkedList<File>();
			//recursively search for specifications
			addTestFiles(testFiles, new File(url.toURI()));
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private final ByteArrayOutputStream logStream = new ByteArrayOutputStream();
	private final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errStream = new ByteArrayOutputStream();
	final static PrintStream origOutput = System.out;
	final static PrintStream origError = System.err;

	protected static void addTestFile(List<File> testFiles, File file, Class<?> clazz) {
		TestUtils.addTestFile(testFiles, file, clazz);
	}

	static void addTestFiles(List<File> testFiles, File file) {
		TestUtils.addTestFiles(testFiles, file);
	}

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(logStream));
		System.setErr(new PrintStream(errStream));
	}

	@After
	public void cleanUpStreams() {
		System.setOut(origOutput);
		System.setErr(origError);
	}

	@Test
	public void performTest() {
		boolean successful = true;
		//check if there are files for testing for this class
		if (testFiles.isEmpty()) {
			TestReport t = TestReport.failure("no test file found!");
			t.print(origOutput, origError);
			successful = false;
		}
		//perform test for all test files, output result, and modify test result if test has failed
		for (File testFile : testFiles) {
			TestReport t = runSpecification(testFile);
			t.print(origOutput, origError);
			if (!t.isSuccessful()) {
				successful = false;
				if (TestUtils.failFast) break;
			}
		}
		//report overall test result
		//test failed if at least one test has failed
		if (!successful)
			Assert.fail("Test failed for class: " + TestAllCasm.class.getSimpleName());

	}

	public TestReport runSpecification(File testFile) {

		List<String> requiredOutputList = TestUtils.getFilteredOutput(testFile, "@require");
		List<String> refusedOutputList = TestUtils.getFilteredOutput(testFile, "@refuse");
		int minSteps = TestUtils.getParameter(testFile, "minsteps");
		if (minSteps <= 0)
			minSteps = 1;
		int maxSteps = TestUtils.getParameter(testFile, "maxsteps");
		if (maxSteps < minSteps)
			maxSteps = minSteps;
		TestEngineDriver td = null;
		int steps = 0;
		try {
			outStream.reset();
			errStream.reset();
			td = TestEngineDriver.newLaunch(testFile.getAbsolutePath(), Tools.getRootFolder(Engine.class)+"/plugins", properties);
			if (TestEngineDriver.TestEngineDriverStatus.stopped.equals(td.getStatus()))
				return TestReport.failure(testFile, "engine is stopped!", steps);

			PrintStream ps = new PrintStream(outStream, false);
			td.setOutputStream(ps);
			for (steps = minSteps; steps <= maxSteps; steps++) {
				td.executeSteps(minSteps);
				minSteps = 1;
				ps.flush();

				String outContent = outStream.toString();
				String errContent = errStream.toString();

				//test if no error has occurred and maybe output error message
				if (!errContent.isEmpty()) {
					String failMessage = "an error occurred!"
							+ "\nerror output:\n"
							+ errContent
							+ "\nactual output:\n" + outContent;
					return TestReport.failure(testFile, failMessage, steps);
				}
				//check if no refused output is contained
				for (String refusedOutput : refusedOutputList) {
					if (outContent.contains(refusedOutput)) {
						String failMessage = "refused output found!"
								+ "\nrefused output:\n"
								+ refusedOutput
								+ "\nactual output:\n" + outContent;
						return TestReport.failure(testFile, failMessage, steps);
					}
				}
				for (String requiredOutput : new LinkedList<String>(requiredOutputList)) {
					if (outContent.contains(requiredOutput))
						requiredOutputList.remove(requiredOutput);
				}
				if (requiredOutputList.isEmpty())
					break;
			}

			// check if no required output is missing after all steps
			if (!requiredOutputList.isEmpty()) {
				String outContent = outStream.toString();
				String failMessage = "missing required output!"
						+ "\nmissing output:\n"
						+ requiredOutputList.get(0)
						+ "\nactual output:\n" + outContent;
				return TestReport.failure(testFile, failMessage, steps - 1);
			}
		}
		catch (Exception e) {
			e.printStackTrace(origOutput);
		}
		finally {
			if (td != null) {
				td.stop();
			}
		}

		if (td == null) {
			String failMessage = "Unable to launch TestEngineDriver";
			return TestReport.failure(testFile, failMessage, steps);
		}
		else if (td.isRunning()) {
			String failMessage = "has a running instance but is stopped!";
			return TestReport.failure(testFile, failMessage, steps);
		}
		else if (steps <= maxSteps /* only if successful */) {
			return TestReport.success(testFile, steps);
		}
		else {
			String failMessage = "no test result!";
			return TestReport.failure(testFile, failMessage, steps);
		}
	}

}
