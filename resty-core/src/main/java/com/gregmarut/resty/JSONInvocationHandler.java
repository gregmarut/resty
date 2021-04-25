/*******************************************************************************
 * Copyright (c) 2014 Greg Marut. All rights reserved. This program and the accompanying materials are made available
 * under the terms of the GNU Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html Contributors: Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.resty;

public class JSONInvocationHandler extends RestInvocationHandler
{
	public static final String JSON_TYPE = "application/json";
	
	private final RestRequestFactory restRequestFactory;
	
	public JSONInvocationHandler(final String rootURL, final RestRequestExecutor restRequestExecutor)
	{
		this(rootURL, restRequestExecutor, new DefaultStatusCodeHandler());
	}
	
	public JSONInvocationHandler(final String rootURL, final RestRequestExecutor restRequestExecutor, final StatusCodeHandler statusCodeHandler)
	{
		super(rootURL, restRequestExecutor, statusCodeHandler);
		
		restRequestFactory = new RestRequestFactory(JSON_TYPE);
	}
	
	@Override
	protected RestRequestFactory getRestRequestFactory()
	{
		return restRequestFactory;
	}
}
