package org.coreasm.engine.test.plugins.graph;

import org.coreasm.engine.test.TestAllCCasm;
import org.junit.BeforeClass;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

public class TestCompilerGraph3_connectedSet extends TestAllCCasm {

	@BeforeClass
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
