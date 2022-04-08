/*
 * Licensed under the Academic Free License version 3.0
 *   http://www.opensource.org/licenses/afl-3.0.php
 *   http://www.coreasm.org/afl-3.0.php
 *
 */

package org.coreasm.engine.plugins.uuid;

import org.coreasm.engine.absstorage.BackgroundElement;
import org.coreasm.engine.absstorage.BooleanElement;
import org.coreasm.engine.absstorage.Element;

import java.util.UUID;

/**
 *	Background of UUIDs.
 *
 *  @author  André Wolski
 *
 */
public class UUIDBackgroundElement extends BackgroundElement {

	/**
	 * Name of the String background
	 */
	public static final String UUID_BACKGROUND_NAME = "UUID";

	/**
	 * Creates a new UUID background.
	 *
	 * @see #UUID_BACKGROUND_NAME
	 */
	public UUIDBackgroundElement() {
	}

	/* (non-Javadoc)
	 * @see org.coreasm.engine.absstorage.BackgroundElement#getNewValue()
	 */
	@Override
	public Element getNewValue() {
		return UUIDElement.NIL;
	}


	/**
	 * Return new UUID element representing specified UUID.
	 *
	 * @param from A <code>java.util.UUID</code> representing UUID element requested.
	 *
	 * @return a <code>UUIDElement</code> representing specified UUID.
	 */
	public UUIDElement getNewValue(UUID from) {
		return new UUIDElement(from);
	}

	/**
	 * Returns a <code>TRUE</code> boolean for
	 * UUID Elements. Otherwise <code>FALSE<code> is returned.
	 *
	 * @see org.coreasm.engine.absstorage.AbstractUniverse#getValue(Element)
	 * @see BooleanElement
	 */
	@Override
	protected BooleanElement getValue(Element e) {
		return (e instanceof UUIDElement) ? BooleanElement.TRUE : BooleanElement.FALSE;
	}

}
