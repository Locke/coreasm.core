package org.coreasm.engine.test.plugins.turboasm;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCasm;

public class TestTurboASM1_iterate extends TestAllCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestTurboASM1_iterate.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestTurboASM1_iterate.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
