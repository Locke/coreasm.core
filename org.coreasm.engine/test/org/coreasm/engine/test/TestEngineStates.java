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

import org.coreasm.engine.Engine;
import org.coreasm.engine.EngineProperties;
import org.coreasm.util.Tools;

public class TestEngineStates {

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
		URL url = TestEngineStates.class.getClassLoader().getResource("./engine_states");

		try {
			testFiles = new LinkedList<>();
			//recursively search for specifications
			TestUtils.addTestFiles(testFiles, new File(url.toURI()));
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
			Assert.fail("Test failed for class: " + TestEngineStates.class.getSimpleName());

	}

	public TestReport runSpecification(File testFile) {
		TestCase testCase = TestUtils.parseTestCase(testFile);
		return runSpecification(testCase);
	}

	private TestReport runSpecification(TestCase testCase) {
		LinkedList<String> remainingRequiredOutputs = new LinkedList<>(testCase.requiredOutputs);
		TestEngineDriver td = null;
		int steps = 0;
		try {
			outStream.reset();
			errStream.reset();
			td = TestEngineDriver.newLaunch(testCase.testFile.getAbsolutePath(), Tools.getRootFolder(Engine.class)+"/plugins", properties);
			if (TestEngineDriver.TestEngineDriverStatus.stopped.equals(td.getStatus()))
				return TestReport.failure(testCase, "engine is stopped!", steps);

			PrintStream ps = new PrintStream(outStream, false);
			td.setOutputStream(ps);
			int minSteps = testCase.minSteps;
			for (steps = testCase.minSteps; steps <= testCase.maxSteps; steps++) {
				td.executeSteps(minSteps);
				minSteps = 1;
				ps.flush();

				String outContent = outStream.toString();
				String errContent = errStream.toString();

				// test if no error has occurred and maybe output error message
				if (!errContent.isEmpty()) {
					return TestReport.failureErrorOutput(testCase, steps, outContent, errContent);
				}

				// check if no refused output is contained
				List<String> occurredRefusedOutputs = new LinkedList<>();
				for (String refusedOutput : testCase.refusedOutputs) {
					if (outContent.contains(refusedOutput)) {
						occurredRefusedOutputs.add(refusedOutput);
					}
				}
				if (!occurredRefusedOutputs.isEmpty()) {
					return TestReport.failureRefusedOutput(testCase, steps, outContent, occurredRefusedOutputs);
				}

				// reduce remaining required output
				remainingRequiredOutputs.removeIf(outContent::contains);
				if (remainingRequiredOutputs.isEmpty())
					break;
			}

			// check if no required output is missing after all steps
			if (!remainingRequiredOutputs.isEmpty()) {
				String outContent = outStream.toString();
				return TestReport.failureMissingOutput(testCase, steps - 1, outContent, remainingRequiredOutputs);
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
			return TestReport.failure(testCase, failMessage, steps);
		}
		else if (td.isRunning()) {
			String failMessage = "has a running instance but is stopped!";
			return TestReport.failure(testCase, failMessage, steps);
		}
		else if (steps <= testCase.maxSteps /* only if successful */) {
			return TestReport.success(testCase, steps);
		}
		else {
			String failMessage = "no test result!";
			return TestReport.failure(testCase, failMessage, steps);
		}
	}

}
