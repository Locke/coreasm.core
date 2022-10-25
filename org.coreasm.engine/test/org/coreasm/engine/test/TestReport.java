package org.coreasm.engine.test;

import java.io.File;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public class TestReport {
	private static final LinkedList<TestReport> reports = new LinkedList<>();
	private final TestCase testCase;
	private final String message;
	private final int steps;
	private final boolean successful;

	private static final String NL = System.lineSeparator();

	public TestReport(TestCase testCase, String message, int steps, boolean successful) {
		this.testCase = testCase;
		this.message = message;
		this.successful = successful;
		this.steps = steps;
		if (testCase != null
				&& !TestReport.reports.isEmpty()
				&&  TestReport.reports.getLast().getFile() == this.testCase.testFile)
			TestAllCasm.origOutput
					.println("Last report has been for the same file. Check if your test produces a unique result.");
		TestReport.reports.add(this);
	}

	private File getFile() {
		return this.testCase == null ? null : this.testCase.testFile;
	}

	public String formatted() {
		String fileFormatted    = this.testCase == null   ? "" : (" of " + this.testCase.testFile.getName());
		String statusFormatted  = this.successful         ? " successful" : " failed";
		String stepsFormatted1  = steps == -1             ? "" : (" after " + steps + (steps == 1 ? " step" : " steps"));
		String stepsFormatted2  = this.testCase == null   ? "" : (" (minSteps: " + this.testCase.minSteps + "; maxSteps: " + this.testCase.maxSteps + ")");
		String messageFormatted = this.message.isEmpty()  ? "" : (": " + this.message);

		return "Test" + fileFormatted + statusFormatted + stepsFormatted1 + stepsFormatted2 + messageFormatted;
	}

	public void print(PrintStream out, PrintStream err) {
		String msg = this.formatted();
		if (this.successful) {
			out.println(msg);
		}
		else {
			err.println(msg);
		}
	}

	public void printTestReports(PrintStream out, PrintStream err) {
		for (TestReport report : reports) {
			report.print(out, err);
		}
	}

	public boolean isSuccessful() {
		return this.successful;
	}

	public String getMessage() {
		return this.message;
	}

	public static TestReport success(TestCase testCase) {
		return new TestReport(testCase, "", -1, true);
	}

	public static TestReport success(TestCase testCase, int steps) {
		return new TestReport(testCase, "", steps, true);
	}

	public static TestReport failure(String message) {
		return new TestReport(null, message, -1, false);
	}

	public static TestReport failure(TestCase testCase, String message) {
		return new TestReport(testCase, message, -1, false);
	}

	public static TestReport failure(TestCase testCase, String message, int steps) {
		return new TestReport(testCase, message, steps, false);
	}

	public static TestReport failureErrorOutput(TestCase testCase, String outContent, String errContent) {
		return TestReport.failureErrorOutput(testCase, -1, outContent, errContent);
	}

	public static TestReport failureErrorOutput(TestCase testCase, int steps, String outContent, String errContent) {
		String message =
				"an error occurred!" + NL +
				"error output:" + NL +
				"----" + NL +
				errContent +
				"----" + NL +
				"actual output:" + NL +
				"----" + NL +
				outContent +
				"----" + NL;
		return new TestReport(testCase, message, steps, false);
	}

	public static TestReport failureRefusedOutput(TestCase testCase, String outContent, List<String> occurredRefusedOutputs) {
		return TestReport.failureRefusedOutput(testCase, -1, outContent, occurredRefusedOutputs);
	}

	public static TestReport failureRefusedOutput(TestCase testCase, int steps, String outContent, List<String> occurredRefusedOutputs) {
		int size = occurredRefusedOutputs.size();
		String summary;
		if (size == 1) {
			summary = "refused output:";
		}
		else {
			summary = size + " refused outputs:";
		}
		String refusedOutputs = String.join(NL + "----" + NL, occurredRefusedOutputs);

		String message =
				"refused output found!" + NL +
				summary + NL +
				"----" + NL +
				refusedOutputs + NL +
				"----" + NL +
				"actual output:" + NL +
				"----" + NL +
				outContent +
				"----" + NL;
		return new TestReport(testCase, message, steps, false);
	}

	public static TestReport failureMissingOutput(TestCase testCase, String outContent, List<String> remainingRequiredOutputs) {
		return TestReport.failureMissingOutput(testCase, -1, outContent, remainingRequiredOutputs);
	}

	public static TestReport failureMissingOutput(TestCase testCase, int steps, String outContent, List<String> remainingRequiredOutputs) {
		int size = remainingRequiredOutputs.size();
		String summary;
		if (size == 1) {
			summary = "missing output:";
		}
		else {
			summary = size + " missing outputs:";
		}
		String missingOutputs = String.join(NL + "----" + NL, remainingRequiredOutputs);

		String message =
				"missing required output!" + NL +
				summary + NL +
				"----" + NL +
				missingOutputs + NL +
				"----" + NL +
				"actual output:" + NL +
				"----" + NL +
				outContent +
				"----" + NL;
		return new TestReport(testCase, message, steps, false);
	}

}
