package com.gregmarut.resty.client.integration;

import com.gregmarut.resty.MethodType;
import com.gregmarut.resty.annotation.HttpHeaders;
import com.gregmarut.resty.annotation.NameValue;
import com.gregmarut.resty.annotation.RestMethod;
import com.gregmarut.resty.annotation.RestProxy;
import com.gregmarut.resty.exception.WebServiceException;

@RestProxy
public interface HeaderProxy
{
	@RestMethod(uri = "/header/verify", method = MethodType.GET)
	@HttpHeaders(@NameValue(name = "token", value = "TOKEN-ABCD1234"))
	void withHeader() throws WebServiceException;
	
	@RestMethod(uri = "/header/verify", method = MethodType.GET)
	@HttpHeaders(@NameValue(name = "token", value = "INVALID"))
	void withInvalidHeader() throws WebServiceException;
	
	@RestMethod(uri = "/header/verify", method = MethodType.GET)
	void withNoHeader() throws WebServiceException;
}
