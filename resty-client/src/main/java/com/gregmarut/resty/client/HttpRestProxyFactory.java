/*******************************************************************************
 * Copyright (c) 2014 Greg Marut. All rights reserved. This program and the accompanying materials
 * are made available
 * under the terms of the GNU Public License v3.0 which accompanies this distribution, and is
 * available at
 * http://www.gnu.org/licenses/gpl.html Contributors: Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.resty.client;

import com.gregmarut.resty.RestProxyFactory;
import com.gregmarut.resty.http.HostDetails;

public class HttpRestProxyFactory extends RestProxyFactory<HttpInvocationHandler>
{
	// holds the factory that will handle new and existing clients
	private final HttpClientFactory httpClientFactory;
	
	public HttpRestProxyFactory(final String hostUrl)
	{
		this(hostUrl, new HttpClientFactory());
	}
	
	public HttpRestProxyFactory(final String hostUrl, final HttpClientFactory httpClientFactory)
	{
		super(new HttpInvocationHandler(httpClientFactory, hostUrl));
		this.httpClientFactory = httpClientFactory;
	}
	
	public HttpRestProxyFactory(final HostDetails hostDetails)
	{
		this(hostDetails, new HttpClientFactory());
	}
	
	public HttpRestProxyFactory(final HostDetails hostDetails, final HttpClientFactory httpClientFactory)
	{
		super(new HttpInvocationHandler(httpClientFactory, hostDetails.getUrl()));
		this.httpClientFactory = httpClientFactory;
	}
	
	public HttpRestProxyFactory(final HttpInvocationHandler httpInvocationHandler)
	{
		super(httpInvocationHandler);
		this.httpClientFactory = httpInvocationHandler.getHttpClientFactory();
	}
	
	public void eraseCookies()
	{
		httpClientFactory.eraseCookies();
	}
	
	public HttpClientFactory getHttpClientFactory()
	{
		return httpClientFactory;
	}
}