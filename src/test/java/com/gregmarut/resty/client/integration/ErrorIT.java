package com.gregmarut.resty.client.integration;

import org.junit.Assert;
import org.junit.Test;

import com.gregmarut.resty.bean.ErrorBean;
import com.gregmarut.resty.client.RestProxyFactory;
import com.gregmarut.resty.client.exception.WebServiceException;
import com.gregmarut.resty.client.http.HostDetails;
import com.gregmarut.resty.client.http.Scheme;

public class ErrorIT
{
	private final RestProxyFactory restProxyFactory;
	private final ErrorProxy errorProxy;
	
	public ErrorIT()
	{
		// set up the rest proxy factory
		HostDetails hostDetails = new HostDetails(Scheme.HTTP, "localhost", null, 8080);
		restProxyFactory = new RestProxyFactory(hostDetails);
		restProxyFactory.setErrorClass(ErrorBean.class);
		errorProxy = restProxyFactory.createProxy(ErrorProxy.class);
	}
	
	@Test
	public void error500Test()
	{
		try
		{
			errorProxy.error500();
			
			Assert.fail("Did not receive expected error.");
		}
		catch (WebServiceException e)
		{
			ErrorBean errorBean = (ErrorBean) e.getErrorEntity();
			
			Assert.assertEquals(500, errorBean.getStatus());
			Assert.assertEquals("500 Error", errorBean.getMessage());
		}
	}
	
	@Test
	public void error500_2Test()
	{
		try
		{
			errorProxy.error500_2();
			
			Assert.fail("Did not receive expected error.");
		}
		catch (WebServiceException e)
		{
			ErrorBean errorBean = (ErrorBean) e.getErrorEntity();
			
			Assert.assertNotNull(errorBean);
			Assert.assertEquals(500, errorBean.getStatus());
			Assert.assertEquals("500 Error", errorBean.getMessage());
		}
	}
}
