package org.coreasm.engine.test.plugins.graph;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCasm;

public class TestGraph7_subgraph extends TestAllCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestGraph7_subgraph.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestGraph7_subgraph.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
