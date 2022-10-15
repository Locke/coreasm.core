package org.coreasm.engine.test.plugins.map;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCCasm;

public class TestCompilerMap1_mapToPairs extends TestAllCCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestMap1_mapToPairs.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestMap1_mapToPairs.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
