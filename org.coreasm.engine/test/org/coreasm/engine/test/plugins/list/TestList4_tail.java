package org.coreasm.engine.test.plugins.list;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.coreasm.engine.test.TestAllCasm;

public class TestList4_tail extends TestAllCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestList4_tail.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestList4_tail.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
