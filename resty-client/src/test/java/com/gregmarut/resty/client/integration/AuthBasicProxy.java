package com.gregmarut.resty.client.integration;

import com.gregmarut.resty.bean.UserBean;
import com.gregmarut.resty.MethodType;
import com.gregmarut.resty.annotation.RestMethod;
import com.gregmarut.resty.annotation.RestProxy;
import com.gregmarut.resty.exception.WebServiceException;

@RestProxy(strict = true)
public interface AuthBasicProxy
{
	@RestMethod(uri = "/auth/basic/me", method = MethodType.GET)
	UserBean getMe() throws WebServiceException;
}
