package com.gregmarut.resty.client;

import org.junit.Before;
import org.junit.Test;

public class NonProxyMethodTest
{
	private static final String ROOT = "http://www.example.com";
	
	private HttpRestProxyFactory restProxyHandler;
	private TestInterfaceProxy proxy;
	
	@Before
	public void init()
	{
		restProxyHandler = new HttpRestProxyFactory(ROOT);
		proxy = restProxyHandler.createProxy(TestInterfaceProxy.class);
	}
	
	@Test
	public void hashCodeTest()
	{
		proxy.hashCode();
	}
	
	@Test
	public void toStringTest()
	{
		proxy.toString();
	}
}
