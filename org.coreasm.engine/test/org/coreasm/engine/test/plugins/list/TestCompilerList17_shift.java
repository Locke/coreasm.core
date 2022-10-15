package org.coreasm.engine.test.plugins.list;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCCasm;

public class TestCompilerList17_shift extends TestAllCCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestList17_shift.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestList17_shift.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
