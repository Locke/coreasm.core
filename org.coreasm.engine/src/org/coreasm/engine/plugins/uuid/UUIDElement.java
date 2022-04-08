/*
 * Licensed under the Academic Free License version 3.0
 *   http://www.opensource.org/licenses/afl-3.0.php
 *   http://www.coreasm.org/afl-3.0.php
 *
 */

package org.coreasm.engine.plugins.uuid;

import org.coreasm.engine.absstorage.Element;

import java.util.List;
import java.util.UUID;

/**
 *	This represents a UUID element;
 *
 *  @author  André Wolski
 *
 */
public class UUIDElement extends Element {

	final static UUIDElement NIL = new UUIDElement(new UUID(0L, 0L));

	protected final UUID uuid;
	protected List<Element> indexedView = null;
	protected String stringValue = null;
	protected String denotationalValue = null;

	/*
	 * Instantiate this UUID element with a new random UUID
	 */
	public UUIDElement(UUID uuid)
	{
		this.uuid = uuid;
	}

	public static UUIDElement randomUUID() {
		return new UUIDElement(UUID.randomUUID());
	}

	@Override
	public String getBackground() {
		return UUIDBackgroundElement.UUID_BACKGROUND_NAME;
	}

	/**
	 * Returns the value of this UUID element
	 * enclosed as String in double-quotes.
	 */
	@Override
	public String denotation() {
		if (denotationalValue == null) {
			denotationalValue = "\"" + this.toString() + "\"";
		}
		return denotationalValue;
	}

	/**
	 * Returns a <code>String</code> representation of
	 * this UUID Element.
	 *
	 * @see Element#toString()
	 */
	@Override
	public String toString() {
		if (stringValue == null) {
			stringValue = uuid.toString();
		}
		return stringValue;
	}

	//----------------------
	// Equality interface
	//----------------------

	/**
	 * Compares this Element to the specified Element.
	 * The result is <code>true</code> if the argument
	 * is not null and is considered to be equal to this Element.
	 *
	 * @param anElement the Element to compare with.
	 * @return <code>true</code> if the Elements are equal; <code>false</code> otherwise.
	 * @throws IllegalArgumentException if <code>anElement</code> is not an instance
	 * of <code>Element</code>
	 */
	@Override
	public boolean equals(Object anElement) {

		// if both java objects are identical, no further checks are required
		if (super.equals(anElement)) {
			return true;
		}
		// else both java objects are not identical, have to check that
		// both are string elements, and both have the same numerical value
		else {
			// both UUID elements
			if (anElement instanceof UUIDElement) {
				UUIDElement otherElement = (UUIDElement)anElement;

				// if the current UUID and the other UUID equal each other
				// then objects are equal
				if (uuid.equals(otherElement.uuid)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Hashcode for UUID elements. Must be overridden because equality is overridden.
	 *
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return uuid.hashCode();
	}

}
