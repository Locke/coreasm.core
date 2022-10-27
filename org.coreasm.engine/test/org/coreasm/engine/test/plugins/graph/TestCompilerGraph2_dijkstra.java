package org.coreasm.engine.test.plugins.graph;

import org.coreasm.engine.test.TestAllCCasm;
import org.junit.BeforeClass;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

public class TestCompilerGraph2_dijkstra extends TestAllCCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestGraph2_dijkstra.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestGraph2_dijkstra.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
