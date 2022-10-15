/*
 * FlattenListTest.java
 *
 * Copyright (C) 2010 Roozbeh Farahbod
 *
 * Last modified by $Author$ on $Date$.
 *
 * Licensed under the Academic Free License version 3.0
 *   http://www.opensource.org/licenses/afl-3.0.php
 *   http://www.coreasm.org/afl-3.0.php
 *
 */

package org.coreasm.engine.plugins.list;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.coreasm.engine.absstorage.Element;
import org.coreasm.engine.absstorage.ElementList;
import org.coreasm.engine.plugins.collection.AbstractListElement;
import org.coreasm.engine.plugins.number.NumberElement;
import org.coreasm.engine.plugins.string.StringElement;

/**
 * @author Roozbeh Farahbod
 *
 */
public class FlattenListTest {

	ListElement[] lists = new ListElement[4];
	FlattenListFunctionElement func = new FlattenListFunctionElement();

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		lists[0] = new ListElement();
		lists[1] = new ListElement(NumberElement.getInstance(5), new StringElement("Hello"), lists[0]);
		lists[2] = new ListElement(NumberElement.getInstance(4), lists[1], NumberElement.getInstance(7));
		lists[3] = new ListElement(lists[2]);
	}

	/**
	 * Test method for {@link org.coreasm.engine.plugins.list.FlattenListFunctionElement#getValue(java.util.List)}.
	 */
	@Test
	public void testGetValue() {
		Element e = func.getValue(new ElementList(lists[2]));
		Assertions.assertTrue(e instanceof AbstractListElement);
		Assertions.assertEquals(4, ((AbstractListElement)e).size());

		e = func.getValue(new ElementList(lists[0]));
		Assertions.assertTrue(e instanceof AbstractListElement);
		Assertions.assertEquals(0, ((AbstractListElement)e).size());
	}

	/**
	 * Test method for {@link org.coreasm.engine.plugins.list.FlattenListFunctionElement#FlattenListFunctionElement()}.
	 */
	@Test
	public void testFlattenListFunctionElement() {
		List<? extends Element> r = func.flattenList(lists[3].getList());
		Assertions.assertEquals(4, r.size());
		System.out.println(r);
		r = func.flattenList(lists[0].getList());
		Assertions.assertEquals(0, r.size());
		System.out.println(r);
	}

}
