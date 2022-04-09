package org.coreasm.engine.test.plugins.queue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.coreasm.engine.test.TestAllCCasm;

public class TestCompilerQueue1 extends TestAllCCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestQueue1.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			getTestFile(testFiles, new File(url.toURI()).getParentFile(), TestQueue1.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
