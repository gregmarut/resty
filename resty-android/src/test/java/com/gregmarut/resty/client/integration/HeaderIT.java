package com.gregmarut.resty.client.integration;

import com.gregmarut.resty.android.URLRestProxyFactory;
import com.gregmarut.resty.bean.ErrorBean;
import com.gregmarut.resty.exception.WebServiceException;
import com.gregmarut.resty.http.HostDetails;
import com.gregmarut.resty.http.Scheme;
import org.junit.Assert;
import org.junit.Test;

public class HeaderIT
{
	private final URLRestProxyFactory restProxyFactory;
	private final HeaderProxy headerProxy;
	
	public HeaderIT()
	{
		// set up the rest proxy factory
		HostDetails hostDetails = new HostDetails(Scheme.HTTP, "localhost", null, 8080);
		restProxyFactory = new URLRestProxyFactory(hostDetails);
		restProxyFactory.setErrorClass(ErrorBean.class);
		headerProxy = restProxyFactory.createProxy(HeaderProxy.class);
	}
	
	@Test
	public void verifyHeaderTest() throws WebServiceException
	{
		headerProxy.withHeader();
	}
	
	@Test
	public void verifyInvalidHeaderTest() throws WebServiceException
	{
		try
		{
			headerProxy.withInvalidHeader();
			
			Assert.fail("Did not receive expected error.");
		}
		catch (WebServiceException e)
		{
			ErrorBean errorBean = (ErrorBean) e.getErrorEntity();
			
			Assert.assertNotNull(errorBean);
			Assert.assertEquals(400, errorBean.getStatus());
			Assert.assertEquals("Invalid Header", errorBean.getMessage());
		}
	}
	
	@Test
	public void verifyNoHeaderTest() throws WebServiceException
	{
		try
		{
			headerProxy.withNoHeader();
			
			Assert.fail("Did not receive expected error.");
		}
		catch (WebServiceException e)
		{
			ErrorBean errorBean = (ErrorBean) e.getErrorEntity();
			
			Assert.assertNotNull(errorBean);
			Assert.assertEquals(400, errorBean.getStatus());
			Assert.assertEquals("Missing Header", errorBean.getMessage());
		}
	}
}
