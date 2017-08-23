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
import com.gregmarut.resty.JSONInvocationHandler;
import com.gregmarut.resty.RestRequestExecutor;
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
	private JSONInvocationHandler invocationHandler;
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
		invocationHandler = new JSONInvocationHandler(
			ROOT,
			new RestRequestExecutor()
			{
				@Override
				public RestResponse executeRequest(final RestRequest restRequest) throws WebServiceException
				{
					//verify the url
					Assert.assertEquals(ROOT + "/test/1/", restRequest.getUrl());
					return new RestResponse(204);
				}
			},
			new DefaultStatusCodeHandler());
		
		restProxyHandler = new HttpRestProxyFactory(invocationHandler);
		proxy = restProxyHandler.createProxy(TestInterfaceProxy.class);
		
		proxy.id(1);
	}
	
	@Test
	public void verifyAcceptsHeader() throws WebServiceException
	{
		invocationHandler = new JSONInvocationHandler(
			ROOT,
			new RestRequestExecutor()
			{
				@Override
				public RestResponse executeRequest(final RestRequest restRequest) throws WebServiceException
				{
					//verify the url
					Assert.assertEquals(ROOT + "/test/1/", restRequest.getUrl());
					return new RestResponse(204);
				}
			},
			new DefaultStatusCodeHandler());
		
		restProxyHandler = new HttpRestProxyFactory(invocationHandler);
		proxy = restProxyHandler.createProxy(TestInterfaceProxy.class);
		
		proxy.id(1);
	}
	
	@Test
	public void testHeaderMethod() throws WebServiceException
	{
		invocationHandler = new JSONInvocationHandler(
			ROOT,
			new RestRequestExecutor()
			{
				@Override
				public RestResponse executeRequest(final RestRequest restRequest) throws WebServiceException
				{
					//verify the url
					Assert.assertEquals("application/json", restRequest.getHeaders().get("Accepts"));
					return new RestResponse(204);
				}
			},
			new DefaultStatusCodeHandler());
		
		restProxyHandler = new HttpRestProxyFactory(invocationHandler);
		proxy = restProxyHandler.createProxy(TestInterfaceProxy.class);
		
		proxy.headerMethodAccepts();
	}
	
	@Test
	public void testParameterMethod() throws WebServiceException
	{
		invocationHandler = new JSONInvocationHandler(
			ROOT,
			new RestRequestExecutor()
			{
				@Override
				public RestResponse executeRequest(final RestRequest restRequest) throws WebServiceException
				{
					//verify the url
					Assert.assertEquals("some value", restRequest.getParameters().get("something"));
					return new RestResponse(204);
				}
			},
			new DefaultStatusCodeHandler());
		
		restProxyHandler = new HttpRestProxyFactory(invocationHandler);
		proxy = restProxyHandler.createProxy(TestInterfaceProxy.class);
		
		proxy.parameterMethodAccepts();
	}
}
