package com.gregmarut.resty.client.integration;

import com.gregmarut.resty.client.MethodType;
import com.gregmarut.resty.client.annotation.Parameter;
import com.gregmarut.resty.client.annotation.RestMethod;
import com.gregmarut.resty.client.annotation.RestProxy;
import com.gregmarut.resty.client.exception.WebServiceException;

@RestProxy(strict = true)
public interface SimpleProxy
{
	@RestMethod(uri = "/simple/one", method = MethodType.GET)
	int getOne() throws WebServiceException;
	
	@RestMethod(uri = "/simple/two", method = MethodType.GET)
	int getTwo() throws WebServiceException;
	
	@RestMethod(uri = "/simple/value/{value}", method = MethodType.GET)
	int getValue(@Parameter("value") int value) throws WebServiceException;
}
