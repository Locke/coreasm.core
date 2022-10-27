package org.coreasm.engine.test.plugins.list;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.coreasm.engine.test.TestAllCCasm;

public class TestCompilerList11_reverse extends TestAllCCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestList11_reverse.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestList11_reverse.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
