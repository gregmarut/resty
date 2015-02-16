package com.gregmarut.resty.client.integration;

import com.gregmarut.resty.client.MethodType;
import com.gregmarut.resty.client.annotation.RestMethod;
import com.gregmarut.resty.client.annotation.RestProxy;
import com.gregmarut.resty.client.exception.WebServiceException;

@RestProxy(strict = true)
public interface ErrorProxy
{
	@RestMethod(uri = "/error/500", method = MethodType.GET)
	void error500() throws WebServiceException;
}
