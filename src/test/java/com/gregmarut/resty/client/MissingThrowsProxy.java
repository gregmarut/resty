package com.gregmarut.resty.client;

import com.gregmarut.resty.client.annotation.RestMethod;
import com.gregmarut.resty.client.annotation.RestProxy;

@RestProxy(strict = true)
public interface MissingThrowsProxy
{
	@RestMethod(uri = "/test", method = MethodType.GET)
	String missingException();
}
