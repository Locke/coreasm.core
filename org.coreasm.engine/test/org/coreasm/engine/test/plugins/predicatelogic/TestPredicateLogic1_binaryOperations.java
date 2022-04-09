package org.coreasm.engine.test.plugins.predicatelogic;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.coreasm.engine.test.TestAllCasm;

public class TestPredicateLogic1_binaryOperations extends TestAllCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestPredicateLogic1_binaryOperations.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			getTestFile(testFiles, new File(url.toURI()).getParentFile(), TestPredicateLogic1_binaryOperations.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
