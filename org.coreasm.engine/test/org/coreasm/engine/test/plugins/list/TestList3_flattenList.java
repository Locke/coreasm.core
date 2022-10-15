package org.coreasm.engine.test.plugins.list;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCasm;

public class TestList3_flattenList extends TestAllCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestList3_flattenList.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestList3_flattenList.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
