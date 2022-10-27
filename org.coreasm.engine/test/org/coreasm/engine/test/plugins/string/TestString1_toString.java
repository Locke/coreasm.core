package org.coreasm.engine.test.plugins.string;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.coreasm.engine.test.TestAllCasm;

public class TestString1_toString extends TestAllCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestString1_toString.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestString1_toString.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
