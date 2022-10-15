package org.coreasm.engine.test.plugins.foreachrule;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCasm;

public class TestForeach2 extends TestAllCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestForeach2.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestForeach2.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
