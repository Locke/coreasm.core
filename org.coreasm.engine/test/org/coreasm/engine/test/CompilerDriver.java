package org.coreasm.engine.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.coreasm.compiler.CompilerOptions;
import org.coreasm.compiler.CoreASMCompiler;
import org.coreasm.engine.CoreASMEngine;
import org.coreasm.engine.CoreASMEngineFactory;
import org.coreasm.engine.Engine;
import org.coreasm.engine.EngineProperties;
import org.coreasm.util.Tools;

public class CompilerDriver {
	public static TestReport runSpecification(File testFile){
		//extract parameters and expected results from the testcase
		List<String> requiredOutputList = TestUtils.getFilteredOutput(testFile, "@require");
		List<String> refusedOutputList = TestUtils.getFilteredOutput(testFile, "@refuse");
		int minSteps = TestUtils.getParameter(testFile, "minsteps");
		System.out.println("minsteps: " + minSteps);
		if (minSteps <= 0)
			minSteps = 1;
		int maxSteps = TestUtils.getParameter(testFile, "maxsteps");
		System.out.println("maxsteps: " + maxSteps);
		if (maxSteps < minSteps)
			maxSteps = minSteps;

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
		System.out.println(Tools.getRootFolder(Engine.class)+"/../org.coreasm.engine-1.7.3-SNAPSHOT.jar");
		options.enginePath = new File(Tools.getRootFolder(Engine.class)+"/../org.coreasm.engine-1.7.3-SNAPSHOT.jar");
		options.outputFile = new File("compiledTest.jar");
		options.removeExistingFiles = true;
		options.SpecificationName = testFile;
		options.terminateOnStepCount = maxSteps + 1;
		System.out.println(options.terminateOnStepCount);
		//Create a compiler using the CoreASM engine
		CoreASMCompiler compiler = new CoreASMCompiler(options, engine);
		try{
			compiler.compile();
		}
		catch(Exception e){
			return TestReport.failure(testFile, "compilation failed: " + e.getMessage());
		}

		//file should now be compiled. Launch it as a separate process; requires a java executable on the PATH
		Process proc = null;
		try {
			proc = Runtime.getRuntime().exec("java -jar compiledTest.jar");

		} catch (IOException e) {
			return TestReport.failure(testFile, "running failed: " + e.getMessage());
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
			return TestReport.failure(testFile, "waiting for process failed: " + e.getMessage());
		}
		stdOutGobbler.stopThread();
		stdErrGobbler.stopThread();

		String outContent = stdOutGobbler.output.toString();
		String errContent = stdErrGobbler.output.toString();

		//check for errors
		if (!errContent.equals("")) {
			return TestReport.failureErrorOutput(testFile, outContent, errContent);
		}
		if(procResult != 0){
			String failMessage = "process terminated with non-zero exit code: " + procResult;
			return TestReport.failure(testFile, failMessage);
		}

		// check output lines

		for (String l : refusedOutputList) {
			if (outContent.contains(l)) {
				return TestReport.failureRefusedOutput(testFile, outContent, l);
			}
		}

		for (String l : requiredOutputList) {
			if (!outContent.contains(l)) {
				return TestReport.failureMissingOutput(testFile, outContent, l);
			}
		}

		return TestReport.success(testFile);
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
