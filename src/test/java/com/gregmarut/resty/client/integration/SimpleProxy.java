package com.gregmarut.resty.client.integration;

import com.gregmarut.resty.client.MethodType;
import com.gregmarut.resty.client.annotation.RestMethod;
import com.gregmarut.resty.client.annotation.RestProxy;

@RestProxy(strict = true)
public interface SimpleProxy
{
	@RestMethod(uri = "/simple/one", method = MethodType.GET)
	int getOne();
	
	@RestMethod(uri = "/simple/two", method = MethodType.GET)
	int getTwo();
}
