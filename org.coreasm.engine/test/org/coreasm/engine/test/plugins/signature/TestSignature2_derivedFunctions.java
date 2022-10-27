package org.coreasm.engine.test.plugins.signature;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.coreasm.engine.test.TestAllCasm;

public class TestSignature2_derivedFunctions extends TestAllCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestSignature2_derivedFunctions.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestSignature2_derivedFunctions.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
