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
package com.gregmarut.resty.exception;

public class InvalidProxyException extends RuntimeException
{
	private static final long serialVersionUID = 6949418977951375766L;
	
	public InvalidProxyException(final String message)
	{
		super(message);
	}
}
