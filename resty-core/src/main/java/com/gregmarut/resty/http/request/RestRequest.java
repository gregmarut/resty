package com.gregmarut.resty.http.request;

import com.gregmarut.resty.MethodType;

import java.util.HashMap;
import java.util.Map;

public class RestRequest
{
	private final String url;
	private final MethodType methodType;
	private final Map<String, String> headers;
	private final Map<String, String> parameters;
	private byte[] data;
	
	public RestRequest(final String url, final MethodType methodType)
	{
		this.url = url;
		this.methodType = methodType;
		this.headers = new HashMap<String, String>();
		this.parameters = new HashMap<String, String>();
	}
	
	public String getUrl()
	{
		return url;
	}
	
	public MethodType getMethodType()
	{
		return methodType;
	}
	
	public void setHeader(final String key, final String value)
	{
		headers.put(key, value);
	}
	
	public Map<String, String> getHeaders()
	{
		return headers;
	}
	
	public void setParameter(final String param, final String value)
	{
		parameters.put(param, value);
	}
	
	public Map<String, String> getParameters()
	{
		return parameters;
	}
	
	public byte[] getData()
	{
		return data;
	}
	
	public void setData(final byte[] data)
	{
		this.data = data;
	}
}
