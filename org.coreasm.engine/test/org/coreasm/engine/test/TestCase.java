package org.coreasm.engine.test;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class TestCase {

	public final File testFile;
	public final List<String> requiredOutputs;
	public final List<String> refusedOutputs;
	public final int minSteps;
	public final int maxSteps;

	public TestCase(File testFile, List<String> requiredOutputs, List<String> refusedOutputs, int minSteps, int maxSteps) {
		this.testFile        = Objects.requireNonNull(testFile);
		this.requiredOutputs = Objects.requireNonNull(requiredOutputs);
		this.refusedOutputs  = Objects.requireNonNull(refusedOutputs);
		this.minSteps = minSteps;
		this.maxSteps = maxSteps;
	}

}
