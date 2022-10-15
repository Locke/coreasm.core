package org.coreasm.engine.test.plugins.turboasm;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCasm;

public class TestTurboASM7_while extends TestAllCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestTurboASM7_while.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestTurboASM7_while.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
