/*******************************************************************************
 * Copyright (c) 2014 Greg Marut.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *    Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.resty.serialization;

/**
 * @author Greg Marut
 */
public final class SerializationException extends Exception
{
	// ** Finals **//
	// holds the serial version UID
	private static final long serialVersionUID = -5991312336095409387L;

	/**
	 * The constructor for SerializationException
	 */
	public SerializationException()
	{
		super("SerializationException: Could not deserialize the object.");
	}
	
	/**
	 * Constucts a SerializationException with a root cause 
	 */
	public SerializationException(final Throwable cause)
	{
		super(cause);
	}
	
	/**
	 * The constructor for SerializationException
	 * 
	 * @param exception
	 */
	public SerializationException(final String exception)
	{
		super(exception);
	}
}
