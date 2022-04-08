/*
 * Licensed under the Academic Free License version 3.0
 *   http://www.opensource.org/licenses/afl-3.0.php
 *   http://www.coreasm.org/afl-3.0.php
 *
 */

package org.coreasm.engine.plugins.uuid;

import org.coreasm.engine.absstorage.Element;
import org.coreasm.engine.absstorage.FunctionElement;

import java.util.List;

/**
 * Implements 'nanoTime' as a monitored function that returns the current value of the running Java Virtual Machine's
 *         high-resolution time source, in nanoseconds.
 *
 * @author  André Wolski
 *
 */
public class NilUUIDFunctionElement extends FunctionElement {

	/** Name of this function */
	public static final String NIL_UUID_FUNC_NAME = "nilUUID";

	public NilUUIDFunctionElement() {
		super();
		setFClass(FunctionClass.fcMonitored);
	}

	/* (non-Javadoc)
	 * @see org.coreasm.engine.absstorage.FunctionElement#getValue(java.util.List)
	 */
	@Override
	public Element getValue(List<? extends Element> args) {
		if (!args.isEmpty())
			return Element.UNDEF;
		else
			return UUIDElement.NIL;
	}

}
