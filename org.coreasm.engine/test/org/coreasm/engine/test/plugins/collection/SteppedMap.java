package org.coreasm.engine.test.plugins.collection;

import org.coreasm.engine.test.TestAllCasm;
import org.junit.BeforeClass;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;

public class SteppedMap extends TestAllCasm {

	@BeforeClass
	public static void onlyOnce() {
		URL url = SteppedMap.class.getClassLoader().getResource(".");

		try {
			testFiles = new LinkedList<File>();
			getTestFile(testFiles, new File(url.toURI()).getParentFile(), SteppedMap.class);
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
