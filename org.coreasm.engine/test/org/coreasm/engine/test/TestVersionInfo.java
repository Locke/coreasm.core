package org.coreasm.engine.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.coreasm.engine.VersionInfo;

public class TestVersionInfo {

	@Test
	public void performVersionInfoTest() {
		Assertions.assertEquals(new VersionInfo(1, 0, 0, ""),  VersionInfo.valueOf("1"));
		Assertions.assertEquals(new VersionInfo(1, 0, 0, ""),  VersionInfo.valueOf("1.0"));
		Assertions.assertEquals(new VersionInfo(1, 0, 0, ""),  VersionInfo.valueOf("1.0.0"));
		Assertions.assertEquals(new VersionInfo(1, 0, 0, ""),  VersionInfo.valueOf("1.0.0-"));
		Assertions.assertEquals(new VersionInfo(1, 0, 0, "a"), VersionInfo.valueOf("1.0.0-a"));

		Assertions.assertEquals(new VersionInfo(0, 1, 0, ""),  VersionInfo.valueOf("0.1"));
		Assertions.assertEquals(new VersionInfo(0, 1, 0, ""),  VersionInfo.valueOf("0.1.0"));
		Assertions.assertEquals(new VersionInfo(0, 1, 0, "a"), VersionInfo.valueOf("0.1.0-a"));

		Assertions.assertEquals(new VersionInfo(0, 0, 1, ""),  VersionInfo.valueOf("0.0.1"));
		Assertions.assertEquals(new VersionInfo(0, 0, 1, ""),  VersionInfo.valueOf("0.0.1-"));
		Assertions.assertEquals(new VersionInfo(0, 0, 1, "a"), VersionInfo.valueOf("0.0.1-a"));

		Assertions.assertEquals(new VersionInfo(1, 2, 3, "a"), VersionInfo.valueOf("1.2.3-a"));
		Assertions.assertEquals(new VersionInfo(1, 2, 3, "a-b"), VersionInfo.valueOf("1.2.3-a-b"));
		Assertions.assertEquals(new VersionInfo(1, 2, 3, "a+b"), VersionInfo.valueOf("1.2.3-a+b"));
		Assertions.assertEquals(new VersionInfo(1, 2, 3, "SNAPSHOT"), VersionInfo.valueOf("1.2.3-SNAPSHOT"));

		Assertions.assertEquals(new VersionInfo(1, 123, 456789, "a"), VersionInfo.valueOf("1.123.456789-a"));
		Assertions.assertEquals("1.123.456789-a+b", VersionInfo.valueOf("1.123.456789-a+b").toString());

		Assertions.assertThrows(IllegalArgumentException.class, () -> new VersionInfo(-1));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new VersionInfo(1, -2));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new VersionInfo(1, 2, -3));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new VersionInfo(1, 2, 3, null));

		Assertions.assertThrows(IllegalArgumentException.class, () -> VersionInfo.valueOf("-1"));
		Assertions.assertEquals(new VersionInfo(1, 0, 0, "2"), VersionInfo.valueOf("1.-2"));
		Assertions.assertEquals(new VersionInfo(1, 2, 0, "3"), VersionInfo.valueOf("1.2.-3"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> VersionInfo.valueOf("1.2.3.4"));

		Assertions.assertThrows(NumberFormatException.class, () -> VersionInfo.valueOf("x"));
		Assertions.assertThrows(NumberFormatException.class, () -> VersionInfo.valueOf("1.y"));
		Assertions.assertThrows(NumberFormatException.class, () -> VersionInfo.valueOf("1.2.z"));
		Assertions.assertThrows(NumberFormatException.class, () -> VersionInfo.valueOf("1.2.3+b"));

		Assertions.assertThrows(NumberFormatException.class, () -> VersionInfo.valueOf("0xdead"));
	}

	@Test
	public void performVersionInfoComparableTest() {
		List<VersionInfo> expected = Arrays.asList(
			new VersionInfo(0, 0, 0, "a"),
			new VersionInfo(0, 0, 1, ""),
			new VersionInfo(0, 0, 1, "a"),
			new VersionInfo(0, 0, 1, "b"),
			new VersionInfo(0, 0, 5, ""),
			new VersionInfo(0, 1, 0, ""),
			new VersionInfo(0, 1, 2, ""),
			new VersionInfo(1, 0, 0, ""),
			new VersionInfo(1, 0, 0, "a"),
			new VersionInfo(2, 0, 2, "a"),
			new VersionInfo(2, 2, 1, "a")
		);

		List<VersionInfo> actual = new ArrayList<>(expected);

		Collections.reverse(actual);

		Collections.sort(actual);

		Assertions.assertEquals(expected, actual);
	}
}
