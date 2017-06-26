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

import com.gregmarut.resty.DefaultStatusCodeHandler;
import com.gregmarut.resty.exception.WebServiceException;
import com.gregmarut.resty.http.request.RestRequest;
import com.gregmarut.resty.http.response.RestResponse;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class RestInvocationHandlerTest
{
	private static final String ROOT = "http://www.example.com";
	private HttpInvocationHandler httpInvocationHandler;
	private HttpRestProxyFactory restProxyHandler;
	
	private TestInterfaceProxy proxy;
	
	//mocked objects
	private CloseableHttpClient mockHttpClient;
	private HttpClientFactory mockHttpClientFactory;
	private CloseableHttpResponse mockHttpResponse;
	private HttpEntity mockEntity;
	private StatusLine mockStatusLine;
	
	@Before
	public void init() throws Exception
	{
		mockHttpClientFactory = Mockito.mock(HttpClientFactory.class);
		mockHttpClient = Mockito.mock(CloseableHttpClient.class);
		mockHttpResponse = Mockito.mock(CloseableHttpResponse.class);
		mockEntity = Mockito.mock(HttpEntity.class);
		mockStatusLine = Mockito.mock(StatusLine.class);
		
		//return the mocked response when the client is executed
		Mockito.when(mockHttpClientFactory.createHttpClient()).thenReturn(mockHttpClient);
		Mockito.when(mockHttpClient.execute((HttpUriRequest) Mockito.any())).thenReturn(mockHttpResponse);
		Mockito.when(mockHttpResponse.getStatusLine()).thenReturn(mockStatusLine);
		Mockito.when(mockHttpResponse.getEntity()).thenReturn(mockEntity);
		Mockito.when(mockStatusLine.getStatusCode()).thenReturn(200);
	}
	
	@Test
	public void verifyURL() throws WebServiceException
	{
		httpInvocationHandler = new HttpInvocationHandler(
			mockHttpClientFactory,
			ROOT,
			new DefaultStatusCodeHandler())
		{
			@Override
			protected RestResponse executeRequest(RestRequest request) throws WebServiceException
			{
				//verify the url
				Assert.assertEquals(ROOT + "/test/1/", request.getUrl());
				return super.executeRequest(request);
			}
		};
		
		restProxyHandler = new HttpRestProxyFactory(httpInvocationHandler);
		proxy = restProxyHandler.createProxy(TestInterfaceProxy.class);
		
		proxy.id(1);
	}
	
	@Test
	public void verifyAcceptsHeader() throws WebServiceException
	{
		httpInvocationHandler = new HttpInvocationHandler(
			mockHttpClientFactory,
			ROOT,
			new DefaultStatusCodeHandler())
		{
			@Override
			protected RestResponse executeRequest(RestRequest request) throws WebServiceException
			{
				//verify the url
				Assert.assertEquals(ROOT + "/test/1/", request.getUrl());
				return super.executeRequest(request);
			}
		};
		
		restProxyHandler = new HttpRestProxyFactory(httpInvocationHandler);
		proxy = restProxyHandler.createProxy(TestInterfaceProxy.class);
		
		proxy.id(1);
	}
	
	@Test
	public void testHeaderMethod() throws WebServiceException
	{
		httpInvocationHandler = new HttpInvocationHandler(
			mockHttpClientFactory,
			ROOT,
			new DefaultStatusCodeHandler())
		{
			@Override
			protected RestResponse executeRequest(RestRequest request) throws WebServiceException
			{
				//verify the headers
				Assert.assertEquals("application/json", request.getHeaders().get("Accepts"));
				return super.executeRequest(request);
			}
		};
		
		restProxyHandler = new HttpRestProxyFactory(httpInvocationHandler);
		proxy = restProxyHandler.createProxy(TestInterfaceProxy.class);
		
		proxy.headerMethodAccepts();
	}
	
	@Test
	public void testParameterMethod() throws WebServiceException
	{
		httpInvocationHandler = new HttpInvocationHandler(
			mockHttpClientFactory,
			ROOT,
			new DefaultStatusCodeHandler())
		{
			@Override
			protected RestResponse executeRequest(RestRequest request) throws WebServiceException
			{
				//verify the parameters
				Assert.assertEquals("some value", request.getParameters().get("something"));
				return super.executeRequest(request);
			}
		};
		
		restProxyHandler = new HttpRestProxyFactory(httpInvocationHandler);
		proxy = restProxyHandler.createProxy(TestInterfaceProxy.class);
		
		proxy.parameterMethodAccepts();
	}
}
