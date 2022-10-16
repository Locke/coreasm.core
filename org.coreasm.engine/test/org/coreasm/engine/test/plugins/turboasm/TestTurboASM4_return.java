package org.coreasm.engine.test.plugins.turboasm;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.coreasm.engine.test.TestAllCasm;

public class TestTurboASM4_return extends TestAllCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestTurboASM4_return.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestTurboASM4_return.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
