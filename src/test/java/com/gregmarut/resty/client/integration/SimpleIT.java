package com.gregmarut.resty.client.integration;

import org.junit.Assert;
import org.junit.Test;

import com.gregmarut.resty.client.RestProxyFactory;
import com.gregmarut.resty.client.exception.WebServiceException;
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
	public void getOneTest() throws WebServiceException
	{
		int result = simpleProxy.getOne();
		
		Assert.assertEquals(1, result);
	}
	
	@Test
	public void getTwoTest() throws WebServiceException
	{
		int result = simpleProxy.getTwo();
		
		Assert.assertEquals(2, result);
	}
	
	@Test
	public void getValueTest() throws WebServiceException
	{
		final int value = 42;
		
		int result = simpleProxy.getValue(value);
		
		Assert.assertEquals(value, result);
	}
}
