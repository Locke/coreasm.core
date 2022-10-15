package org.coreasm.engine.test.plugins.graph;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCasm;

public class TestGraph3_connectedSet extends TestAllCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestGraph3_connectedSet.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestGraph3_connectedSet.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
