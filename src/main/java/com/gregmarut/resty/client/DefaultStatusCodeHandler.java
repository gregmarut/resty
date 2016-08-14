/*******************************************************************************
 * Copyright (c) 2014 Greg Marut.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * Contributors:
 * Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.resty.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;

import com.gregmarut.resty.client.annotation.Expected;
import com.gregmarut.resty.client.exception.status.BadRequestException;
import com.gregmarut.resty.client.exception.status.ForbiddenException;
import com.gregmarut.resty.client.exception.status.StatusCodeException;
import com.gregmarut.resty.client.exception.status.UnauthorizedException;

public class DefaultStatusCodeHandler implements StatusCodeHandler
{
	// holds the map of status codes to exception classes
	private final Map<Integer, Class<? extends StatusCodeException>> statusCodeExceptionMap;
	
	public DefaultStatusCodeHandler()
	{
		statusCodeExceptionMap = new HashMap<Integer, Class<? extends StatusCodeException>>();
		
		// map the default exceptions and status codes
		statusCodeExceptionMap.put(HttpStatus.SC_UNAUTHORIZED, UnauthorizedException.class);
		statusCodeExceptionMap.put(HttpStatus.SC_FORBIDDEN, ForbiddenException.class);
		statusCodeExceptionMap.put(HttpStatus.SC_BAD_REQUEST, BadRequestException.class);
	}
	
	/**
	 * Determines whether or not a call was successful by checking the status codes
	 * 
	 * @param statusCode
	 * @param expectedStatusCode
	 * @return
	 */
	public boolean isSuccessful(final int statusCode, final int expectedStatusCode)
	{
		// if no expected status code was set and the status code is in the 200s then return
		if (Expected.DEFAULT_STATUS_CODE == expectedStatusCode && statusCode >= 200 && statusCode < 300)
		{
			return true;
		}
		// if the status code return was what was expected
		else if (expectedStatusCode == statusCode)
		{
			return true;
		}
		// otherwise, this was not successful
		else
		{
			return false;
		}
	}
	
	public void verifyStatusCode(final int statusCode, final int expectedStatusCode, final Object errorEntity)
		throws StatusCodeException
	{
		// check to see if the status codes determine that it was not successful
		if (!isSuccessful(statusCode, expectedStatusCode))
		{
			throw determineException(statusCode, errorEntity);
		}
	}
	
	/**
	 * Determine the status code
	 * 
	 * @param statusCode
	 * @return
	 */
	public StatusCodeException determineException(final int statusCode, final Object errorEntity)
	{
		StatusCodeException exception;
		
		switch (statusCode)
		{
			case HttpStatus.SC_UNAUTHORIZED:
				exception = new UnauthorizedException();
				break;
			case HttpStatus.SC_FORBIDDEN:
				exception = new ForbiddenException();
				break;
			case HttpStatus.SC_BAD_REQUEST:
				exception = new BadRequestException();
				break;
			default:
				exception = new StatusCodeException(statusCode);
				break;
		}
		
		exception.setErrorEntity(errorEntity);
		
		return exception;
	}
	
	/**
	 * Returns the map of status codes to exceptions
	 * 
	 * @return
	 */
	public Map<Integer, Class<? extends StatusCodeException>> getStatusCodeExceptionMap()
	{
		return statusCodeExceptionMap;
	}
}
