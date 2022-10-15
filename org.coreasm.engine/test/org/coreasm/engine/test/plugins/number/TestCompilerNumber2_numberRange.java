package org.coreasm.engine.test.plugins.number;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCCasm;

public class TestCompilerNumber2_numberRange extends TestAllCCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestNumber2_numberRange.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestNumber2_numberRange.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
