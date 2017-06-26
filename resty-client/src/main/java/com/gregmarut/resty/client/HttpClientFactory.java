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

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Provides a factory for creating HttpClients
 * 
 * @author Greg Marut
 */
public class HttpClientFactory
{
	public static final int DEFAULT_TIMEOUT = 3000;
	public static final int DEFAULT_SO_TIMEOUT = 5000;
	
	// holds the cookie store to share cookies between clients
	protected final CookieStore cookieStore;
	
	// holds the map of headers to use for each request
	protected final Map<String, String> httpHeaders;
	
	// holds the set of http interceptors
	protected final Set<HttpRequestInterceptor> httpRequestInterceptors;
	protected final Set<HttpResponseInterceptor> httpResponseInterceptors;
	
	public HttpClientFactory()
	{
		// create a new cookie store
		cookieStore = new BasicCookieStore();
		
		// create the map of headers to attach with each request
		httpHeaders = new HashMap<String, String>();
		
		// assign the set of interceptors
		httpRequestInterceptors = new HashSet<HttpRequestInterceptor>();
		httpResponseInterceptors = new HashSet<HttpResponseInterceptor>();
	}
	
	/**
	 * Create a new Http Client
	 * 
	 * @return
	 */
	public CloseableHttpClient createHttpClient()
	{
		// create a new default client
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		clientBuilder.setConnectionManager(connectionManager);
		
		// assign the same cookie store
		clientBuilder.setDefaultCookieStore(cookieStore);
		
		// for each of the request interceptors
		for (HttpRequestInterceptor interceptor : httpRequestInterceptors)
		{
			// add the interceptor to the client
			clientBuilder.addInterceptorLast(interceptor);
		}
		
		// for each of the response interceptors
		for (HttpResponseInterceptor interceptor : httpResponseInterceptors)
		{
			// add the interceptor to the client
			clientBuilder.addInterceptorLast(interceptor);
		}
		
		return clientBuilder.build();
	}
	
	/**
	 * Create the default Http parameters
	 * 
	 * @return
	 */
	protected void addDefaultHttpParams(HttpClientBuilder clientBuilder)
	{
		RequestConfig.Builder config = RequestConfig.custom();
		
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		config.setSocketTimeout(DEFAULT_SO_TIMEOUT);
		
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		config.setConnectTimeout(DEFAULT_TIMEOUT);
		
		clientBuilder.setDefaultRequestConfig(config.build());
	}
	
	public void addHttpRequestInterceptor(final HttpRequestInterceptor interceptor)
	{
		httpRequestInterceptors.add(interceptor);
	}
	
	public void removeHttpRequestInterceptor(final HttpRequestInterceptor interceptor)
	{
		httpRequestInterceptors.remove(interceptor);
	}
	
	public void addHttpResponseInterceptor(final HttpResponseInterceptor interceptor)
	{
		httpResponseInterceptors.add(interceptor);
	}
	
	public void removeHttpResponseInterceptor(final HttpResponseInterceptor interceptor)
	{
		httpResponseInterceptors.remove(interceptor);
	}
	
	/**
	 * Puts a header to be used for each http request
	 *
	 * @param name
	 * @param value
	 */
	public void putHeader(final String name, final String value)
	{
		httpHeaders.put(name, value);
	}
	
	/**
	 * Returns the map of headers
	 *
	 * @return
	 */
	public Map<String, String> getHttpHeaders()
	{
		return httpHeaders;
	}
	
	public CookieStore getCookieStore()
	{
		return cookieStore;
	}
	
	public void eraseCookies()
	{
		cookieStore.clear();
	}
}
