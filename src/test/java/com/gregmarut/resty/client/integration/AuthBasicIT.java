package com.gregmarut.resty.client.integration;

import org.junit.Assert;
import org.junit.Test;

import com.gregmarut.resty.bean.ErrorBean;
import com.gregmarut.resty.bean.UserBean;
import com.gregmarut.resty.client.RestProxyFactory;
import com.gregmarut.resty.client.exception.WebServiceException;
import com.gregmarut.resty.client.http.HostDetails;
import com.gregmarut.resty.client.http.Scheme;
import com.gregmarut.resty.server.config.security.basic.ErrorEntity;
import com.gregmarut.resty.server.config.security.basic.TestUserDetailsService;

public class AuthBasicIT
{
	private final RestProxyFactory restProxyFactory;
	private final AuthBasicProxy authBasicProxy;
	
	public AuthBasicIT()
	{
		// set up the rest proxy factory
		HostDetails hostDetails = new HostDetails(Scheme.HTTP, "localhost", null, 8080);
		restProxyFactory = new RestProxyFactory(hostDetails);
		restProxyFactory.setErrorClass(ErrorBean.class);
		authBasicProxy = restProxyFactory.createProxy(AuthBasicProxy.class);
		
		// set the basic auth credentials
		restProxyFactory.setErrorClass(ErrorEntity.class);
		restProxyFactory.setHttpAuthenticationCredentials(TestUserDetailsService.TEST_USERNAME,
			TestUserDetailsService.TEST_PASSWORD);
	}
	
	@Test
	public void getMeTest() throws WebServiceException
	{
		UserBean userBean = authBasicProxy.getMe();
		
		Assert.assertEquals("Greg", userBean.getFirstName());
		Assert.assertEquals("Marut", userBean.getLastName());
	}
}
