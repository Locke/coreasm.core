package org.coreasm.engine.test.plugins.list;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.coreasm.engine.test.TestAllCCasm;

public class TestCompilerList6_cons extends TestAllCCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestList6_cons.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			getTestFile(testFiles, new File(url.toURI()).getParentFile(), TestList6_cons.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
