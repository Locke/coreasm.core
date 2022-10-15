package org.coreasm.engine.test.plugins.graph;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCasm;

public class TestGraph6_findCyclesWithVertex extends TestAllCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestGraph6_findCyclesWithVertex.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestGraph6_findCyclesWithVertex.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
