package com.gregmarut.resty.http.response;

import java.util.HashMap;
import java.util.Map;

public class RestResponseBuilder
{
	private final Map<String, String> headers;
	private int statusCode;
	private byte[] data;
	
	private RestResponseBuilder()
	{
		this.headers = new HashMap<String, String>();
	}
	
	public RestResponseBuilder setHeader(final String key, final String value)
	{
		headers.put(key, value);
		return this;
	}
	
	public RestResponseBuilder setStatusCode(final int statusCode)
	{
		this.statusCode = statusCode;
		return this;
	}
	
	public RestResponseBuilder setData(final byte[] data)
	{
		this.data = data;
		return this;
	}
	
	public RestResponse build()
	{
		return new RestResponse(statusCode, data, headers);
	}
	
	public static RestResponseBuilder create()
	{
		return new RestResponseBuilder();
	}
}
