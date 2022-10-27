package org.coreasm.engine.test.plugins.number;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.coreasm.engine.test.TestAllCCasm;

public class TestCompilerNumber1_functions extends TestAllCCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestNumber1_functions.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestNumber1_functions.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
