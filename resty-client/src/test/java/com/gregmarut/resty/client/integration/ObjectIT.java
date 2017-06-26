package com.gregmarut.resty.client.integration;

import com.gregmarut.resty.bean.ErrorBean;
import com.gregmarut.resty.bean.UserBean;
import com.gregmarut.resty.client.HttpRestProxyFactory;
import com.gregmarut.resty.exception.WebServiceException;
import com.gregmarut.resty.http.HostDetails;
import com.gregmarut.resty.http.Scheme;
import org.junit.Assert;
import org.junit.Test;

public class ObjectIT
{
	private final HttpRestProxyFactory restProxyFactory;
	private final ObjectProxy objectProxy;
	
	public ObjectIT()
	{
		// set up the rest proxy factory
		HostDetails hostDetails = new HostDetails(Scheme.HTTP, "localhost", null, 8080);
		restProxyFactory = new HttpRestProxyFactory(hostDetails);
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
	
	public void getBytesTest() throws WebServiceException
	{
		byte[] bytes = objectProxy.getBytes();
		
		Assert.assertEquals(new String(bytes), "Hello");
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
