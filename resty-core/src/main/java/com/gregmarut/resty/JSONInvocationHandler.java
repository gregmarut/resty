/*******************************************************************************
 * Copyright (c) 2014 Greg Marut. All rights reserved. This program and the accompanying materials are made available
 * under the terms of the GNU Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html Contributors: Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.resty;

import com.gregmarut.resty.serialization.GsonSerializer;
import com.gregmarut.resty.serialization.Serializer;

public abstract class JSONInvocationHandler<C> extends RestInvocationHandler<C>
{
	public static final String JSON_TYPE = "application/json";
	
	private final RestRequestFactory restRequestFactory;
	private final GsonSerializer serializer;
	
	public JSONInvocationHandler(final String rootURL)
	{
		this(rootURL, new DefaultStatusCodeHandler());
	}
	
	public JSONInvocationHandler(final String rootURL,
		final StatusCodeHandler statusCodeHandler)
	{
		this(rootURL, statusCodeHandler, new GsonSerializer());
	}
	
	public JSONInvocationHandler(final String rootURL, final StatusCodeHandler statusCodeHandler,
		final GsonSerializer serializer)
	{
		super(rootURL, statusCodeHandler);
		
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
