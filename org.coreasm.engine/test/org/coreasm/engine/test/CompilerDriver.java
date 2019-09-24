package org.coreasm.engine.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.coreasm.compiler.CompilerOptions;
import org.coreasm.compiler.CoreASMCompiler;
import org.coreasm.engine.CoreASMEngine;
import org.coreasm.engine.CoreASMEngineFactory;
import org.coreasm.engine.Engine;
import org.coreasm.engine.EngineProperties;
import org.coreasm.util.Tools;

public class CompilerDriver {

	public static TestReport runSpecification(File testFile) {
		TestCase testCase = TestUtils.parseTestCase(testFile);
		return runSpecification(testCase);
	}

	private static TestReport runSpecification(TestCase testCase) {
		//create a CoreASM engine
		CoreASMEngine engine = CoreASMEngineFactory.createEngine();
		engine.setClassLoader(CoreASMEngineFactory.class.getClassLoader());
		String pluginFolders = Tools.getRootFolder(Engine.class)+"/plugins";
		if (System.getProperty(EngineProperties.PLUGIN_FOLDERS_PROPERTY) != null)
			pluginFolders += EngineProperties.PLUGIN_FOLDERS_DELIM
					+ System.getProperty(EngineProperties.PLUGIN_FOLDERS_PROPERTY);
		engine.setProperty(EngineProperties.PLUGIN_FOLDERS_PROPERTY, pluginFolders);
		engine.enqueueInitialize();
		engine.waitWhileBusy();
		//Create compiler options, set the maximum step count and activate necessary output
		CompilerOptions options = new CompilerOptions();
		File targetDirectory = new File(Tools.getRootFolder(Engine.class)).getParentFile();
		options.enginePath = new File(targetDirectory, "org.coreasm.engine-library.jar");
		System.out.println("enginePath: " + options.enginePath);
		options.outputFile = new File(targetDirectory, "compiledTest.jar");
		options.removeExistingFiles = true;
		options.SpecificationName = testCase.testFile;
		options.terminateOnStepCount = testCase.maxSteps + 1;
		System.out.println(options.terminateOnStepCount);
		//Create a compiler using the CoreASM engine
		CoreASMCompiler compiler = new CoreASMCompiler(options, engine);
		try{
			compiler.compile();
		}
		catch(Exception e){
			return TestReport.failure(testCase, "compilation failed: " + e.getMessage());
		}

		//file should now be compiled. Launch it as a separate process; requires a java executable on the PATH
		Process proc = null;
		try {
			proc = Runtime.getRuntime().exec("java -jar compiledTest.jar");

		} catch (IOException e) {
			return TestReport.failure(testCase, "running failed: " + e.getMessage());
		}

		StreamGobbler stdOutGobbler = new StreamGobbler(proc.getInputStream());
		StreamGobbler stdErrGobbler = new StreamGobbler(proc.getErrorStream());
		Thread stdOutGobblerThread = new Thread(stdOutGobbler);
		Thread stdErrGobblerThread = new Thread(stdErrGobbler);
		stdOutGobblerThread.start();
		stdErrGobblerThread.start();
		int procResult = 0;
		try{
			procResult = proc.waitFor();
		}
		catch(Exception e){
			return TestReport.failure(testCase, "waiting for process failed: " + e.getMessage());
		}
		stdOutGobbler.stopThread();
		stdErrGobbler.stopThread();

		String outContent = stdOutGobbler.output.toString();
		String errContent = stdErrGobbler.output.toString();

		//check for errors
		if (!errContent.equals("")) {
			return TestReport.failureErrorOutput(testCase, outContent, errContent);
		}
		if(procResult != 0){
			String failMessage = "process terminated with non-zero exit code: " + procResult;
			return TestReport.failure(testCase, failMessage);
		}

		// check output lines

		List<String> occurredRefusedOutputs = new LinkedList<>();
		for (String l : testCase.refusedOutputs) {
			if (outContent.contains(l)) {
				occurredRefusedOutputs.add(l);
			}
		}
		if (!occurredRefusedOutputs.isEmpty()) {
			return TestReport.failureRefusedOutput(testCase, outContent, occurredRefusedOutputs);
		}

		List<String> remainingRequiredOutputs = new LinkedList<>();
		for (String l : testCase.requiredOutputs) {
			if (!outContent.contains(l)) {
				remainingRequiredOutputs.add(l);
			}
		}
		if (!remainingRequiredOutputs.isEmpty()) {
			return TestReport.failureMissingOutput(testCase, outContent, remainingRequiredOutputs);
		}

		return TestReport.success(testCase);
	}
}

class StreamGobbler implements Runnable {
	public final StringBuilder output;
	private final InputStream stream;
	private volatile boolean quit;

	public StreamGobbler(InputStream in) {
		stream = in;
		quit = false;
		output = new StringBuilder();
	}

	@Override
	public void run() {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
			boolean initialLine = true;

			while (!quit) {
				String line = br.readLine();
				if (line == null) continue;
				if (initialLine) {
					initialLine = false;
					output.append(line);
				}
				else {
					output.append("\n").append(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopThread(){
		this.quit = true;
	}

}
