/*******************************************************************************
 * Copyright (c) 2014 Greg Marut. All rights reserved. This program and the accompanying materials
 * are made available
 * under the terms of the GNU Public License v3.0 which accompanies this distribution, and is
 * available at
 * http://www.gnu.org/licenses/gpl.html Contributors: Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.resty.client;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.gregmarut.resty.client.annotation.RestMethod;
import com.gregmarut.resty.client.annotation.RestProxy;
import com.gregmarut.resty.client.authentication.AuthenticationProvider;
import com.gregmarut.resty.client.exception.InvalidProxyException;
import com.gregmarut.resty.client.exception.WebServiceException;
import com.gregmarut.resty.client.http.HostDetails;

public class RestProxyFactory
{
	// holds the factory that will handle new and existing clients
	private final HttpClientFactory httpClientFactory;
	
	// holds the invocation handler
	private final RestInvocationHandler restInvocationHandler;
	
	public RestProxyFactory(final String hostUrl)
	{
		this(hostUrl, new HttpClientFactory());
	}
	
	public RestProxyFactory(final String hostUrl, final HttpClientFactory httpClientFactory)
	{
		this.httpClientFactory = httpClientFactory;
		this.restInvocationHandler = new JSONInvocationHandler(httpClientFactory, hostUrl);
	}
	
	public RestProxyFactory(final HostDetails hostDetails)
	{
		this(hostDetails, new HttpClientFactory());
	}
	
	public RestProxyFactory(final HostDetails hostDetails, final HttpClientFactory httpClientFactory)
	{
		this.httpClientFactory = httpClientFactory;
		this.restInvocationHandler = new JSONInvocationHandler(httpClientFactory, hostDetails.getUrl());
	}
	
	public RestProxyFactory(final RestInvocationHandler restInvocationHandler)
	{
		httpClientFactory = restInvocationHandler.getHttpClientFactory();
		this.restInvocationHandler = restInvocationHandler;
	}
	
	/**
	 * Sets an authentication provider for the api requests
	 * 
	 * @param authenticationProvider
	 */
	public void setAuthenticationProvider(final AuthenticationProvider authenticationProvider)
	{
		restInvocationHandler.setAuthenticationProvider(authenticationProvider);
	}
	
	public void clearAuthenticationProvider()
	{
		restInvocationHandler.setAuthenticationProvider(null);
	}
	
	public void eraseCookies()
	{
		httpClientFactory.eraseCookies();
	}
	
	/**
	 * Creates a proxy of the specified interface. Interface must be properly annotated with
	 * {@link RestProxy}.
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T createProxy(final Class<T> clazz)
	{
		// check to see if this class is not an interface
		if (!clazz.isInterface())
		{
			throw new InvalidProxyException(clazz.getName() + " must be an interface.");
		}
		
		// retrieve the RestProxy annotation from the class
		RestProxy restProxy = clazz.getAnnotation(RestProxy.class);
		
		// make sure that this annotation is not null
		if (null == restProxy)
		{
			throw new InvalidProxyException(clazz.getName() + " is missing annotation " + RestProxy.class.getName());
		}
		
		// check to see if this interface should be strictly checked
		if (restProxy.strict())
		{
			// get a list of all of the methods declared on this class
			Method[] methods = clazz.getDeclaredMethods();
			
			// for each method on the interface
			for (Method method : methods)
			{
				// get the rest method annotation
				RestMethod restMethod = method.getAnnotation(RestMethod.class);
				
				// check to see if this annotation is null
				if (null == restMethod)
				{
					throw new InvalidProxyException(clazz.getName() + "." + method.getName()
						+ " is missing annotation " + RestMethod.class.getName());
				}
				
				// validate that the method declares the webservice exception
				validateThrowsExeception(method);
			}
		}
		
		// create and return a new instance of the proxy
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]
		{
			clazz
		}, restInvocationHandler);
	}
	
	/**
	 * Validates that the method provided throws the {@link WebServiceException}
	 * 
	 * @param method
	 */
	private void validateThrowsExeception(final Method method)
	{
		// retrieve the array of declared exception types
		Class<?>[] exceptionTypes = method.getExceptionTypes();
		
		// make sure that the exception types array is not null
		if (null != exceptionTypes)
		{
			// for each of the exception types
			for (Class<?> type : exceptionTypes)
			{
				// check to see if this type is assignable from the webservice exception
				if (WebServiceException.class.isAssignableFrom(type))
				{
					return;
				}
			}
		}
		
		throw new InvalidProxyException(method.getDeclaringClass().getName() + "." + method.getName()
			+ " is missing throws " + WebServiceException.class.getName());
	}
	
	public HttpClientFactory getHttpClientFactory()
	{
		return httpClientFactory;
	}
	
	/**
	 * Sets the class that represents an error entity
	 * 
	 * @param errorClass
	 */
	public void setErrorClass(Class<?> errorClass)
	{
		restInvocationHandler.setErrorClass(errorClass);
	}
}
