package org.coreasm.engine.test;

import java.io.File;
import java.io.PrintStream;
import java.util.LinkedList;

public class TestReport {
	private static final LinkedList<TestReport> reports = new LinkedList<>();
	private final File file;
	private final String message;
	private final int steps;
	private final boolean successful;

	private static final String NL = System.lineSeparator();

	public TestReport(File file, String message, int steps, boolean successful) {
		this.file = file;
		this.message = message;
		this.successful = successful;
		this.steps = steps;
		if (!TestReport.reports.isEmpty()
				&& TestReport.reports.getLast().getFile() == this.file)
			TestAllCasm.origOutput
					.println("Last report has been for the same file. Check if your test produces a unique result.");
		reports.add(this);
	}

	private File getFile() {
		return this.file;
	}

	public String formatted() {
		String fileFormatted    = this.file == null       ? "" : (" of " + this.file.getName());
		String statusFormatted  = this.successful         ? " successful" : " failed";
		String stepsFormatted   = steps == -1             ? "" : (" after " + steps + (steps == 1 ? " step" : " steps"));
		String messageFormatted = this.message.isEmpty()  ? "" : (": " + this.message);

		return "Test" + fileFormatted + statusFormatted + stepsFormatted + messageFormatted;
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

	public static TestReport success(File file) {
		return new TestReport(file, "", -1, true);
	}

	public static TestReport success(File file, int steps) {
		return new TestReport(file, "", steps, true);
	}

	public static TestReport failure(String message) {
		return new TestReport(null, message, -1, false);
	}

	public static TestReport failure(File file, String message) {
		return new TestReport(file, message, -1, false);
	}

	public static TestReport failure(File file, String message, int steps) {
		return new TestReport(file, message, steps, false);
	}

	public static TestReport failureErrorOutput(File file, String outContent, String errContent) {
		return TestReport.failureErrorOutput(file, -1, outContent, errContent);
	}

	public static TestReport failureErrorOutput(File file, int steps, String outContent, String errContent) {
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
		return new TestReport(file, message, steps, false);
	}

	public static TestReport failureRefusedOutput(File file, String outContent, String refusedOutput) {
		return TestReport.failureRefusedOutput(file, -1, outContent, refusedOutput);
	}

	public static TestReport failureRefusedOutput(File file, int steps, String outContent, String refusedOutput) {
		String message =
				"refused output found!" + NL +
				"refused output:" + NL +
				"----" + NL +
				refusedOutput + NL +
				"----" + NL +
				"actual output:" + NL +
				"----" + NL +
				outContent +
				"----" + NL;
		return new TestReport(file, message, steps, false);
	}

	public static TestReport failureMissingOutput(File file, String outContent, String missingOutput) {
		return TestReport.failureMissingOutput(file, -1, outContent, missingOutput);
	}

	public static TestReport failureMissingOutput(File file, int steps, String outContent, String missingOutput) {
		String message =
				"missing required output!" + NL +
				"missing output:" + NL +
				"----" + NL +
				missingOutput + NL +
				"----" + NL +
				"actual output:" + NL +
				"----" + NL +
				outContent +
				"----" + NL;
		return new TestReport(file, message, steps, false);
	}

}
