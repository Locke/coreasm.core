package org.coreasm.engine.test.plugins.predicatelogic;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCCasm;

public class TestCompilerPredicateLogic4_not_notEqual extends TestAllCCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestPredicateLogic4_not_notEqual.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestPredicateLogic4_not_notEqual.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
