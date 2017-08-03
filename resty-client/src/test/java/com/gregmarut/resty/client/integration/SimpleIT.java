package com.gregmarut.resty.client.integration;

import com.gregmarut.resty.client.HttpRestProxyFactory;
import com.gregmarut.resty.exception.WebServiceException;
import com.gregmarut.resty.http.HostDetails;
import com.gregmarut.resty.http.Scheme;
import org.junit.Assert;
import org.junit.Test;

public class SimpleIT
{
	private final HttpRestProxyFactory restProxyFactory;
	private final SimpleProxy simpleProxy;
	
	public SimpleIT()
	{
		// set up the rest proxy factory
		HostDetails hostDetails = new HostDetails(Scheme.HTTP, "localhost", null, 8080);
		restProxyFactory = new HttpRestProxyFactory(hostDetails);
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
