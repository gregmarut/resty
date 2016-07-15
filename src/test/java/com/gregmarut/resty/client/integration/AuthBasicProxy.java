package com.gregmarut.resty.client.integration;

import com.gregmarut.resty.bean.UserBean;
import com.gregmarut.resty.client.MethodType;
import com.gregmarut.resty.client.annotation.RestMethod;
import com.gregmarut.resty.client.annotation.RestProxy;
import com.gregmarut.resty.client.exception.WebServiceException;

@RestProxy(strict = true)
public interface AuthBasicProxy
{
	@RestMethod(uri = "/auth/basic/me", method = MethodType.GET)
	UserBean getMe() throws WebServiceException;
}
