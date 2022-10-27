package org.coreasm.engine.test.plugins.predicatelogic;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.coreasm.engine.test.TestAllCasm;

public class TestPredicateLogic3_forallHold extends TestAllCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = TestPredicateLogic3_forallHold.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			addTestFile(testFiles, new File(url.toURI()).getParentFile(), TestPredicateLogic3_forallHold.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
