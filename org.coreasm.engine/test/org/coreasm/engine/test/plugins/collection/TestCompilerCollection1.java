package org.coreasm.engine.test.plugins.collection;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCCasm;

public class TestCompilerCollection1 extends TestAllCCasm {

	@BeforeAll
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
