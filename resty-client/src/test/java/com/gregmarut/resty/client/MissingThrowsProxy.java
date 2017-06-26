package com.gregmarut.resty.client;

import com.gregmarut.resty.MethodType;
import com.gregmarut.resty.annotation.RestMethod;
import com.gregmarut.resty.annotation.RestProxy;

@RestProxy(strict = true)
public interface MissingThrowsProxy
{
	@RestMethod(uri = "/test", method = MethodType.GET)
	String missingException();
}
