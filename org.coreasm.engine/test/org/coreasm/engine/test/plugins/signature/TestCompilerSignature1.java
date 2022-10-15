package org.coreasm.engine.test.plugins.signature;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCCasm;

public class TestCompilerSignature1 extends TestAllCCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestSignature1.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestSignature1.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
