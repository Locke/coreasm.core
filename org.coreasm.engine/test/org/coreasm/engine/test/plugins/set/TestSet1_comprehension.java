package org.coreasm.engine.test.plugins.set;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.coreasm.engine.test.TestAllCasm;

public class TestSet1_comprehension extends TestAllCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestSet1_comprehension.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestSet1_comprehension.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
