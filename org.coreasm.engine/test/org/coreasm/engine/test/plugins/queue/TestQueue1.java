package org.coreasm.engine.test.plugins.queue;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCasm;

public class TestQueue1 extends TestAllCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestQueue1.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestQueue1.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
