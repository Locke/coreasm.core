package org.coreasm.engine.test;

import java.io.File;
import java.util.List;
import java.util.Objects;

import org.coreasm.engine.CoreASMEngine;

public class DetailedTestCase extends TestCase {

	public enum TestCaseStepDoType {
		waitWhileBusy, enqueueStep
	}

	public enum TestCaseStepCheckType {
		engineStatus
	}

	public final List<TestCaseStep> testCaseSteps;

	public DetailedTestCase(TestCase base, List<TestCaseStep> testCaseSteps) {
		this(base.testFile, base.requiredOutputs, base.refusedOutputs, base.minSteps, base.maxSteps, testCaseSteps);
	}

	public DetailedTestCase(File testFile, List<String> requiredOutputs, List<String> refusedOutputs, int minSteps, int maxSteps, List<TestCaseStep> testCaseSteps) {
		super(testFile, requiredOutputs, refusedOutputs, minSteps, maxSteps);
		this.testCaseSteps = Objects.requireNonNull(testCaseSteps);
	}

	interface TestCaseStep {
		static TestCaseStep parse(String line) {
			if (line.startsWith("@do")) {
				return TestCaseStepDo.parse(line);
			}
			else if (line.startsWith("@check")) {
				return TestCaseStepCheck.parse(line);
			}
			else {
				throw new IllegalArgumentException("unknown TestCaseStep for: '" + line + "'");
			}
		}
	}

	static class TestCaseStepDo implements TestCaseStep {
		public final TestCaseStepDoType type;

		TestCaseStepDo(TestCaseStepDoType type) {
			this.type = type;
		}

		@Override
		public String toString() {
			return "TestCaseStepDo_" + type.toString();
		}

		static TestCaseStepDo parse(String line) {
			String[] args = line.split("\\s");
			assert(args.length == 2);
			assert(Objects.equals(args[0], "@do"));

			TestCaseStepDoType type = TestCaseStepDoType.valueOf(args[1]);

			return new TestCaseStepDo(type);
		}

	}

	static class TestCaseStepCheck implements TestCaseStep {

		public final TestCaseStepCheckType type;
		public final Object right;

		TestCaseStepCheck(TestCaseStepCheckType type, Object right) {
			this.type = type;
			this.right = right;
		}

		@Override
		public String toString() {
			return "TestCaseStepCheck_" + type.toString() + "=" + right;
		}

		static TestCaseStepCheck parse(String line) {
			String[] args = line.split("\\s");
			assert(args.length == 3);
			assert(Objects.equals(args[0], "@check"));

			TestCaseStepCheckType type = TestCaseStepCheckType.valueOf(args[1]);
			Object right;
			if (type == TestCaseStepCheckType.engineStatus) {
				right = CoreASMEngine.EngineMode.valueOf(args[2]);
			}
			else {
				throw new IllegalArgumentException("unknown TestCaseStepCheckType for: '" + line + "'");
			}

			return new TestCaseStepCheck(type, right);
		}

	}

}
