package com.gregmarut.resty.client.integration;

import org.junit.Assert;
import org.junit.Test;

import com.gregmarut.resty.bean.ErrorBean;
import com.gregmarut.resty.bean.UserBean;
import com.gregmarut.resty.client.RestProxyFactory;
import com.gregmarut.resty.client.exception.WebServiceException;
import com.gregmarut.resty.client.http.HostDetails;
import com.gregmarut.resty.client.http.Scheme;

public class ObjectIT
{
	private final RestProxyFactory restProxyFactory;
	private final ObjectProxy objectProxy;
	
	public ObjectIT()
	{
		// set up the rest proxy factory
		HostDetails hostDetails = new HostDetails(Scheme.HTTP, "localhost", null, 8080);
		restProxyFactory = new RestProxyFactory(hostDetails);
		restProxyFactory.setErrorClass(ErrorBean.class);
		objectProxy = restProxyFactory.createProxy(ObjectProxy.class);
	}
	
	@Test
	public void getUserTest() throws WebServiceException
	{
		UserBean userBean = objectProxy.getUser();
		
		Assert.assertEquals("Greg", userBean.getFirstName());
		Assert.assertEquals("Marut", userBean.getLastName());
	}
	
	@Test
	public void putUserTest() throws WebServiceException
	{
		UserBean userBean = new UserBean();
		userBean.setFirstName("Greg");
		userBean.setLastName("Marut");
		
		objectProxy.putUser(userBean);
	}
	
	@Test
	public void putWrongUserTest()
	{
		UserBean userBean = new UserBean();
		userBean.setFirstName("John");
		userBean.setLastName("Doe");
		
		try
		{
			objectProxy.putUser(userBean);
			
			Assert.fail("Did not receive expected error.");
		}
		catch (WebServiceException e)
		{
			ErrorBean errorBean = (ErrorBean) e.getErrorEntity();
			
			Assert.assertEquals(400, errorBean.getStatus());
			Assert.assertEquals("Wrong User", errorBean.getMessage());
		}
	}
}
