package org.coreasm.engine.test.plugins.caserule;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.coreasm.engine.test.TestAllCCasm;

public class TestCompilerCase1 extends TestAllCCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestCase1.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestCase1.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
