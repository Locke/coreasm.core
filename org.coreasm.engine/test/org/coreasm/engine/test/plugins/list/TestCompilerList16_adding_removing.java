package org.coreasm.engine.test.plugins.list;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.coreasm.engine.test.TestAllCCasm;

public class TestCompilerList16_adding_removing extends TestAllCCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestList16_adding_removing.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			getTestFile(testFiles, new File(url.toURI()).getParentFile(), TestList16_adding_removing.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
