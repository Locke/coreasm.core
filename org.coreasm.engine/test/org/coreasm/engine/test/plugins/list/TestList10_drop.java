package org.coreasm.engine.test.plugins.list;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCasm;

public class TestList10_drop extends TestAllCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestList10_drop.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestList10_drop.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
