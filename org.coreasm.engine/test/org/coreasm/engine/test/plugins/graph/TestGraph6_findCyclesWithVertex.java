package org.coreasm.engine.test.plugins.graph;

import org.coreasm.engine.test.TestAllCasm;
import org.junit.BeforeClass;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

public class TestGraph6_findCyclesWithVertex extends TestAllCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestGraph6_findCyclesWithVertex.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			getTestFile(testFiles, new File(url.toURI()).getParentFile(), TestGraph6_findCyclesWithVertex.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
