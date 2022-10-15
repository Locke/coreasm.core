package org.coreasm.engine.test.plugins.graph;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCasm;

public class TestGraph1_construct extends TestAllCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestGraph1_construct.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestGraph1_construct.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
