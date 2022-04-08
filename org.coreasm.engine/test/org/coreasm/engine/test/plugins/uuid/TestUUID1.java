package org.coreasm.engine.test.plugins.uuid;

import org.coreasm.engine.test.TestAllCasm;
import org.junit.BeforeClass;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

public class TestUUID1 extends TestAllCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestUUID1.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			getTestFile(testFiles, new File(url.toURI()).getParentFile(), TestUUID1.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
