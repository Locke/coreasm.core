package org.coreasm.engine.test.plugins.map;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCCasm;

public class TestCompilerMap4_remove extends TestAllCCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestMap4_remove.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestMap4_remove.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
