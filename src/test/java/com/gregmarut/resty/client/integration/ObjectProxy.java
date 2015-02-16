package com.gregmarut.resty.client.integration;

import org.apache.http.HttpStatus;

import com.gregmarut.resty.bean.UserBean;
import com.gregmarut.resty.client.MethodType;
import com.gregmarut.resty.client.annotation.Expected;
import com.gregmarut.resty.client.annotation.RestMethod;
import com.gregmarut.resty.client.annotation.RestProxy;
import com.gregmarut.resty.client.exception.WebServiceException;

@RestProxy(strict = true)
public interface ObjectProxy
{
	@RestMethod(uri = "/object/user", method = MethodType.GET)
	UserBean getUser() throws WebServiceException;
	
	@RestMethod(uri = "/object/user", method = MethodType.PUT)
	@Expected(statusCode = HttpStatus.SC_CREATED)
	void putUser(UserBean userBean) throws WebServiceException;
}
