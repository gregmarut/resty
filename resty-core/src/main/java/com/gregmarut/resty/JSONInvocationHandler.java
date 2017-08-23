/*******************************************************************************
 * Copyright (c) 2014 Greg Marut. All rights reserved. This program and the accompanying materials are made available
 * under the terms of the GNU Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html Contributors: Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.resty;

import com.gregmarut.resty.serialization.GsonSerializer;
import com.gregmarut.resty.serialization.Serializer;

public class JSONInvocationHandler extends RestInvocationHandler
{
	public static final String JSON_TYPE = "application/json";
	
	private final RestRequestFactory restRequestFactory;
	private final GsonSerializer serializer;
	
	public JSONInvocationHandler(final String rootURL, final RestRequestExecutor restRequestExecutor)
	{
		this(rootURL, restRequestExecutor, new DefaultStatusCodeHandler());
	}
	
	public JSONInvocationHandler(final String rootURL, final RestRequestExecutor restRequestExecutor,
		final StatusCodeHandler statusCodeHandler)
	{
		this(rootURL, restRequestExecutor, statusCodeHandler, new GsonSerializer());
	}
	
	public JSONInvocationHandler(final String rootURL, final RestRequestExecutor restRequestExecutor, final StatusCodeHandler statusCodeHandler,
		final GsonSerializer serializer)
	{
		super(rootURL, restRequestExecutor, statusCodeHandler);
		
		restRequestFactory = new RestRequestFactory(JSON_TYPE);
		this.serializer = serializer;
	}
	
	@Override
	protected RestRequestFactory getRestRequestFactory()
	{
		return restRequestFactory;
	}
	
	@Override
	protected Serializer getSerializer()
	{
		return serializer;
	}
}
