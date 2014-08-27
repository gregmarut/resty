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
package com.gregmarut.resty.client;

import com.gregmarut.resty.serialization.GsonSerializer;
import com.gregmarut.resty.serialization.Serializer;

public class JSONInvocationHandler extends RestInvocationHandler
{
	private final RestRequestFactory restRequestFactory;
	private final Serializer serializer;
	
	public JSONInvocationHandler(final HttpClientFactory httpClientFactory, final String rootURL)
	{
		this(httpClientFactory, rootURL, new DefaultStatusCodeHandler());
	}
	
	public JSONInvocationHandler(final HttpClientFactory httpClientFactory, final String rootURL, final StatusCodeHandler statusCodeHandler)
	{
		super(httpClientFactory, rootURL, statusCodeHandler);
		
		restRequestFactory = new RestRequestFactory(RestRequestFactory.JSON_TYPE);
		serializer = new GsonSerializer();
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
