package org.coreasm.engine.test.plugins.schedulingpolicies;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCCasm;

public class TestCompilerSchedul1 extends TestAllCCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestSchedul1.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestSchedul1.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
