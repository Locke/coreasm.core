package org.coreasm.engine.test.plugins.debuginfo;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.coreasm.engine.test.TestAllCCasm;

public class TestCompilerDebug1 extends TestAllCCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestDebug1.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestDebug1.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
