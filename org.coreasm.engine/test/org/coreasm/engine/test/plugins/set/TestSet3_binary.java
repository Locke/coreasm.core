package org.coreasm.engine.test.plugins.set;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCasm;

public class TestSet3_binary extends TestAllCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestSet3_binary.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestSet3_binary.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
