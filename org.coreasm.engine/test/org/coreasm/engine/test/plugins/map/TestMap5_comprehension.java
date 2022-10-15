package org.coreasm.engine.test.plugins.map;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCasm;

public class TestMap5_comprehension extends TestAllCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestMap5_comprehension.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestMap5_comprehension.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
