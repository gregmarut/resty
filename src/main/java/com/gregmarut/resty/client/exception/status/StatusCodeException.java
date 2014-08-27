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

import com.gregmarut.resty.client.exception.WebServiceException;

public class StatusCodeException extends WebServiceException
{
	private static final long serialVersionUID = 1L;
	
	private final int statusCode;
	
	public StatusCodeException(final int statusCode)
	{
		super("Server returned an unexpected response: " + statusCode);
		
		this.statusCode = statusCode;
	}
	
	public int getStatusCode()
	{
		return statusCode;
	}
}
