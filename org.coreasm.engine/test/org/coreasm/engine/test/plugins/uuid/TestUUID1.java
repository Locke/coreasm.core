package org.coreasm.engine.test.plugins.uuid;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCasm;

public class TestUUID1 extends TestAllCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestUUID1.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestUUID1.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
