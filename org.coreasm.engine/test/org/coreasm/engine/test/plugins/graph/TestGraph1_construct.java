package org.coreasm.engine.test.plugins.graph;

import org.coreasm.engine.test.TestAllCasm;
import org.junit.BeforeClass;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

public class TestGraph1_construct extends TestAllCasm {

	@BeforeClass
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
