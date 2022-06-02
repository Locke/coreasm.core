package org.coreasm.engine.test.plugins.graph;

import org.coreasm.engine.test.TestAllCasm;
import org.junit.BeforeClass;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

public class TestGraph8_asUndirectedGraph extends TestAllCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestGraph8_asUndirectedGraph.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			getTestFile(testFiles, new File(url.toURI()).getParentFile(), TestGraph8_asUndirectedGraph.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
