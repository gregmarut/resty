package com.gregmarut.resty.client.integration;

import com.gregmarut.resty.MethodType;
import com.gregmarut.resty.annotation.Parameter;
import com.gregmarut.resty.annotation.RestMethod;
import com.gregmarut.resty.annotation.RestProxy;
import com.gregmarut.resty.async.Async;

@RestProxy
public interface SimpleAsyncProxy
{
	@RestMethod(uri = "/simple/one", method = MethodType.GET)
	Async<Integer> getOne();
	
	@RestMethod(uri = "/simple/two", method = MethodType.GET)
	Async<Integer> getTwo();
	
	@RestMethod(uri = "/simple/value/{value}", method = MethodType.GET)
	Async<Integer> getValue(@Parameter("value") int value);
}
