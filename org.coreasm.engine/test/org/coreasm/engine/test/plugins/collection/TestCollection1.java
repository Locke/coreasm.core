package org.coreasm.engine.test.plugins.collection;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.coreasm.engine.test.TestAllCasm;

public class TestCollection1 extends TestAllCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestCollection1.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestCollection1.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
