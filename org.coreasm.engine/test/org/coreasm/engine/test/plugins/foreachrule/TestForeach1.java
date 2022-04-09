package org.coreasm.engine.test.plugins.foreachrule;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.coreasm.engine.test.TestAllCasm;

public class TestForeach1 extends TestAllCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestForeach1.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			getTestFile(testFiles, new File(url.toURI()).getParentFile(), TestForeach1.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
