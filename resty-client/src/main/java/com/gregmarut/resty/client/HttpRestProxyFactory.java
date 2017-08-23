/*******************************************************************************
 * Copyright (c) 2014 Greg Marut. All rights reserved. This program and the accompanying materials
 * are made available
 * under the terms of the GNU Public License v3.0 which accompanies this distribution, and is
 * available at
 * http://www.gnu.org/licenses/gpl.html Contributors: Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.resty.client;

import com.gregmarut.resty.JSONInvocationHandler;
import com.gregmarut.resty.RestProxyFactory;
import com.gregmarut.resty.http.HostDetails;

public class HttpRestProxyFactory extends RestProxyFactory<JSONInvocationHandler>
{
	// holds the factory that will handle new and existing clients
	private final HttpClientFactory httpClientFactory;
	
	public HttpRestProxyFactory(final String hostUrl)
	{
		this(hostUrl, new HttpClientFactory());
	}
	
	public HttpRestProxyFactory(final String hostUrl, final HttpClientFactory httpClientFactory)
	{
		super(new JSONInvocationHandler(hostUrl, new HttpRestRequestExecutor(httpClientFactory)));
		this.httpClientFactory = httpClientFactory;
	}
	
	public HttpRestProxyFactory(final HostDetails hostDetails)
	{
		this(hostDetails, new HttpClientFactory());
	}
	
	public HttpRestProxyFactory(final HostDetails hostDetails, final HttpClientFactory httpClientFactory)
	{
		super(new JSONInvocationHandler(hostDetails.getUrl(), new HttpRestRequestExecutor(httpClientFactory)));
		this.httpClientFactory = httpClientFactory;
	}
	
	public HttpRestProxyFactory(final JSONInvocationHandler invocationHandler)
	{
		super(invocationHandler);
		this.httpClientFactory = new HttpClientFactory();
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