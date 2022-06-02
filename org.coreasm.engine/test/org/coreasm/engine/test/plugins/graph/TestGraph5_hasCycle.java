package org.coreasm.engine.test.plugins.graph;

import org.coreasm.engine.test.TestAllCasm;
import org.junit.BeforeClass;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

public class TestGraph5_hasCycle extends TestAllCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestGraph5_hasCycle.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			getTestFile(testFiles, new File(url.toURI()).getParentFile(), TestGraph5_hasCycle.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
