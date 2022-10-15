package org.coreasm.engine.test.plugins.list;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCCasm;

public class TestCompilerList7_nth extends TestAllCCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestList7_nth.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestList7_nth.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
