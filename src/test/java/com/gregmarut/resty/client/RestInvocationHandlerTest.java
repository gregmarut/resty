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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.gregmarut.resty.client.exception.WebServiceException;

public class RestInvocationHandlerTest
{
	private static final String ROOT = "http://www.example.com";
	private RestInvocationHandler restInvocationHandler;
	private RestProxyFactory restProxyHandler;
	
	private TestInterfaceProxy proxy;
	
	//mocked objects
	private HttpClient mockHttpClient;
	private HttpClientFactory mockHttpClientFactory;
	private HttpResponse mockHttpResponse;
	private HttpEntity mockEntity;
	private StatusLine mockStatusLine;
	
	@Before
	public void init() throws Exception
	{
		mockHttpClientFactory = Mockito.mock(HttpClientFactory.class);
		mockHttpClient = Mockito.mock(HttpClient.class);
		mockHttpResponse = Mockito.mock(HttpResponse.class);
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
		restInvocationHandler = new JSONInvocationHandler(
			mockHttpClientFactory,
			ROOT,
			new DefaultStatusCodeHandler())
		{
			@Override
			protected HttpResponse executeRequest(HttpClient httpClient, HttpUriRequest request) throws WebServiceException
			{
				//verify the url
				Assert.assertEquals(ROOT + "/test/1/", request.getURI().toString());
				return super.executeRequest(httpClient, request);
			}
		};
		
		restProxyHandler = new RestProxyFactory(restInvocationHandler);
		proxy = restProxyHandler.createProxy(TestInterfaceProxy.class);
		
		proxy.id(1);
	}
	
	@Test
	public void verifyAcceptsHeader() throws WebServiceException
	{
		restInvocationHandler = new JSONInvocationHandler(
			mockHttpClientFactory,
			ROOT,
			new DefaultStatusCodeHandler())
		{
			@Override
			protected HttpResponse executeRequest(HttpClient httpClient, HttpUriRequest request) throws WebServiceException
			{
				//verify the url
				Assert.assertEquals(ROOT + "/test/1/", request.getURI().toString());
				return super.executeRequest(httpClient, request);
			}
		};
		
		restProxyHandler = new RestProxyFactory(restInvocationHandler);
		proxy = restProxyHandler.createProxy(TestInterfaceProxy.class);
		
		proxy.id(1);
	}

	@Test
	public void testHeaderMethod() throws WebServiceException
	{
		restInvocationHandler = new JSONInvocationHandler(
			mockHttpClientFactory,
			ROOT,
			new DefaultStatusCodeHandler())
		{
			@Override
			protected HttpResponse executeRequest(HttpClient httpClient, HttpUriRequest request) throws WebServiceException
			{
				//verify the headers
				Assert.assertEquals("Accepts: application/json", request.getFirstHeader("Accepts").toString());
				return super.executeRequest(httpClient, request);
			}
		};
		
		restProxyHandler = new RestProxyFactory(restInvocationHandler);
		proxy = restProxyHandler.createProxy(TestInterfaceProxy.class);
		
		proxy.headerMethodAccepts();
	}
	
	@Test
	public void testParameterMethod() throws WebServiceException
	{
		restInvocationHandler = new JSONInvocationHandler(
			mockHttpClientFactory,
			ROOT,
			new DefaultStatusCodeHandler())
		{
			@Override
			protected HttpResponse executeRequest(HttpClient httpClient, HttpUriRequest request) throws WebServiceException
			{
				//verify the parameters
				Assert.assertEquals("some value", request.getParams().getParameter("something"));
				return super.executeRequest(httpClient, request);
			}
		};
		
		restProxyHandler = new RestProxyFactory(restInvocationHandler);
		proxy = restProxyHandler.createProxy(TestInterfaceProxy.class);
		
		proxy.parameterMethodAccepts();
	}
}
