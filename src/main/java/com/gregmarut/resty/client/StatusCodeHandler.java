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

import com.gregmarut.resty.client.exception.status.StatusCodeException;

public interface StatusCodeHandler
{
	public boolean isSuccessful(int statusCode, int expectedStatusCode);
	
	public void verifyStatusCode(int statusCode, int expectedStatusCode, Object errorEntity) throws StatusCodeException;
	
	public StatusCodeException determineException(final int statusCode, final Object errorEntity);
}
