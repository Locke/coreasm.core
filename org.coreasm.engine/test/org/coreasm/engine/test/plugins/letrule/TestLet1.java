package org.coreasm.engine.test.plugins.letrule;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.coreasm.engine.test.TestAllCasm;

public class TestLet1 extends TestAllCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestLet1.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestLet1.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
