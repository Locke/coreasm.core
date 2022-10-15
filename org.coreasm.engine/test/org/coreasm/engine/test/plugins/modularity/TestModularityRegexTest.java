package org.coreasm.engine.test.plugins.modularity;

import java.util.regex.Matcher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.coreasm.engine.plugins.modularity.ModularityPlugin;

public class TestModularityRegexTest {

	@Test
	public void testRegex() {
		String[] testLines = new String[]{
				"include a",
				"include a.b",
				"include ..\\a.b",
				"include ../a.b",

				// quoted
				"include \"a.b\"",
				"include \"..\\a.b\"",
				"include \"../a.b\"",

				// with white spaces
				"\tinclude a",
				"include\ta",
				"include a\t",

				// with comment at the end
				"include a // foo",
				"include a /* foo */"
		};

		for (String line : testLines) {
			Matcher includeMatcher = ModularityPlugin.includePattern.matcher(line);

			Assertions.assertTrue(includeMatcher.find(), line);
		}
	}
}
