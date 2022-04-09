package org.coreasm.engine.test.plugins.map;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.coreasm.engine.test.TestAllCCasm;

public class TestCompilerMap5_comprehension extends TestAllCCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestMap5_comprehension.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			getTestFile(testFiles, new File(url.toURI()).getParentFile(), TestMap5_comprehension.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
