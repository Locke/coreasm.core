package org.coreasm.engine.test.plugins.modularity;

import org.coreasm.engine.plugins.modularity.ModularityPlugin;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.regex.Matcher;

public class ModularityRegexTest {

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

            assertTrue(line, includeMatcher.find());
        }
    }
}
