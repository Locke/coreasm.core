/*
 * VersionInfo.java 	1.0 	$Revision: 243 $
 *
 * Copyright (C) 2006 Roozbeh Farahbod
 *
 * Last modified by $Author: rfarahbod $ on $Date: 2011-03-29 02:05:21 +0200 (Di, 29 Mrz 2011) $.
 *
 * Licensed under the Academic Free License version 3.0
 *   http://www.opensource.org/licenses/afl-3.0.php
 *   http://www.coreasm.org/afl-3.0.php
 *
 */

package org.coreasm.engine;

import java.util.StringTokenizer;

/**
 * Holds version information of a module.
 *
 * @author Roozbeh Farahbod
 */
public record VersionInfo(int major, int minor, int patch, String postfix) implements Comparable<VersionInfo> {

	public VersionInfo(int major) {
		this(major, 0, 0, "");
	}

	public VersionInfo(int major, int minor) {
		this(major, minor, 0, "");
	}

	public VersionInfo(int major, int minor, int patch) {
		this(major, minor, patch, "");
	}

	public VersionInfo(int major, int minor, int patch, String postfix) {
		if (major < 0 || minor < 0 || patch < 0)
			throw new IllegalArgumentException("All version components must be positive");
		if (postfix == null)
			throw new IllegalArgumentException("postfix must not be null");
		this.major = major;
		this.minor = minor;
		this.patch = patch;
		this.postfix = postfix;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder().append(major).append(".").append(minor).append(".").append(patch);
		if (!postfix.isEmpty())
			sb.append("-").append(postfix);
		return sb.toString();
	}

	@Override
	public int compareTo(VersionInfo o) {
		int c1 = Integer.compare(this.major, o.major);
		if (c1 != 0) {
			return c1;
		} else {
			int c2 = Integer.compare(this.minor, o.minor);
			if (c2 != 0) {
				return c2;
			} else {
				int c3 = Integer.compare(this.patch, o.patch);
				if (c3 != 0) {
					return c3;
				} else {
					return this.postfix.compareTo(o.postfix);
				}
			}
		}
	}

	/**
	 * Parses the given string argument into a
	 * VersionInfo object instance. The string should be
	 * in the following format:
	 * <p>
	 * major.minor.build-postfix
	 * <p>
	 * The major field is the only mandatory field. The rest,
	 * if provided, should appear in the given order; i.e., you cannot
	 * have a version information with only major and build.
	 *
	 * @param str
	 */
	public static VersionInfo valueOf(String str) {
		int major;
		int minor = 0;
		int build = 0;
		String postfix = "";

		String[] post = str.split("-", 2);
		if (post.length > 1)
			postfix = post[1];

		StringTokenizer tokenizer = new StringTokenizer(post[0], ".");
		if (tokenizer.hasMoreTokens()) {
			major = Integer.parseInt(tokenizer.nextToken(), 10);
			if (tokenizer.hasMoreTokens()) {
				minor = Integer.parseInt(tokenizer.nextToken(), 10);
				if (tokenizer.hasMoreTokens()) {
					build = Integer.parseInt(tokenizer.nextToken(), 10);
					if (tokenizer.hasMoreTokens())
						throw new IllegalArgumentException("to many version components, expected max 3");
				}
			}
		}
		else {
			throw new IllegalArgumentException("no major version component");
		}

		return new VersionInfo(major, minor, build, postfix);
	}
}
