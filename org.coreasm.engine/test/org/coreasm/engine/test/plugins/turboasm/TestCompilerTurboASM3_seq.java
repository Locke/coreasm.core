package org.coreasm.engine.test.plugins.turboasm;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.coreasm.engine.test.TestAllCCasm;

public class TestCompilerTurboASM3_seq extends TestAllCCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestTurboASM3_seq.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestTurboASM3_seq.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
