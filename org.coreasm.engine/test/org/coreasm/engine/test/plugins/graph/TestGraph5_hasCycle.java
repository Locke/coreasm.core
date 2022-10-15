package org.coreasm.engine.test.plugins.graph;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCasm;

public class TestGraph5_hasCycle extends TestAllCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestGraph5_hasCycle.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestGraph5_hasCycle.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
