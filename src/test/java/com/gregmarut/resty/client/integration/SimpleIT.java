package com.gregmarut.resty.client.integration;

import org.junit.Assert;
import org.junit.Test;

import com.gregmarut.resty.client.RestProxyFactory;
import com.gregmarut.resty.client.http.HostDetails;
import com.gregmarut.resty.client.http.Scheme;

public class SimpleIT
{
	private final RestProxyFactory restProxyFactory;
	private final SimpleProxy simpleProxy;
	
	public SimpleIT()
	{
		// set up the rest proxy factory
		HostDetails hostDetails = new HostDetails(Scheme.HTTP, "localhost", null, 8080);
		restProxyFactory = new RestProxyFactory(hostDetails);
		simpleProxy = restProxyFactory.createProxy(SimpleProxy.class);
	}
	
	@Test
	public void getOneTest()
	{
		int result = simpleProxy.getOne();
		
		Assert.assertEquals(1, result);
	}
	
	@Test
	public void getOneTwo()
	{
		int result = simpleProxy.getTwo();
		
		Assert.assertEquals(2, result);
	}
}
