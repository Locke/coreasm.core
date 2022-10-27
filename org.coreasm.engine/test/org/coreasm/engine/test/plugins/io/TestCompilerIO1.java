package org.coreasm.engine.test.plugins.io;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.coreasm.engine.test.TestAllCCasm;

public class TestCompilerIO1 extends TestAllCCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestIO1.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestIO1.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
