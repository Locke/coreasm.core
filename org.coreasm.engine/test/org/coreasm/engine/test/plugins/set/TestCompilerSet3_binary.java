package org.coreasm.engine.test.plugins.set;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.coreasm.engine.test.TestAllCCasm;

public class TestCompilerSet3_binary extends TestAllCCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestSet3_binary.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			getTestFile(testFiles, new File(url.toURI()).getParentFile(), TestSet3_binary.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
