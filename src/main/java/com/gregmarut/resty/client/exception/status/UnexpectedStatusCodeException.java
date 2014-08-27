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
package com.gregmarut.resty.client.exception.status;

public class UnexpectedStatusCodeException extends StatusCodeException
{
	private static final long serialVersionUID = 1L;
	
	public UnexpectedStatusCodeException(final int statusCode)
	{
		super(statusCode);
	}
}
