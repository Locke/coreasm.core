package org.coreasm.engine.test.plugins.predicatelogic;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.jupiter.api.BeforeAll;

import org.coreasm.engine.test.TestAllCasm;

public class TestPredicateLogic2_ExistsIn extends TestAllCasm {

	@BeforeAll
	public static void onlyOnce() {
		URL url = TestPredicateLogic2_ExistsIn.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestPredicateLogic2_ExistsIn.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
