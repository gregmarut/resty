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

import org.junit.Before;
import org.junit.Test;

import com.gregmarut.resty.client.exception.ConflictingAnnotationException;
import com.gregmarut.resty.client.exception.InvalidProxyException;
import com.gregmarut.resty.client.exception.MultipleBodyException;
import com.gregmarut.resty.client.exception.WebServiceException;

public class RestProxyHandlerTest
{
	private static final String ROOT = "http://www.example.com";
	
	private RestProxyFactory restProxyHandler;
	private TestInterfaceProxy proxy;
	
	@Before
	public void init()
	{
		restProxyHandler = new RestProxyFactory(ROOT);
		proxy = restProxyHandler.createProxy(TestInterfaceProxy.class);
	}
	
	@Test(expected = MultipleBodyException.class)
	public void testInvalidTwoEntities() throws WebServiceException
	{
		proxy.invalidTwoEntities("entityA", "entityB");
	}
	
	@Test(expected = ConflictingAnnotationException.class)
	public void testInvalidTwoAnnotations() throws WebServiceException
	{
		proxy.invalidTwoAnnotations("entityA");
	}
	
	@Test(expected = InvalidProxyException.class)
	public void testMissingTypeAnnotationProxy()
	{
		restProxyHandler.createProxy(MissingTypeAnnotationProxy.class);
	}
	
	@Test(expected = InvalidProxyException.class)
	public void testMissingMethodAnnotationProxy()
	{
		restProxyHandler.createProxy(MissingMethodAnnotationProxy.class);
	}
	
	@Test(expected = InvalidProxyException.class)
	public void testMissingExceptionAnnotationProxy()
	{
		restProxyHandler.createProxy(MissingThrowsProxy.class);
	}
}
