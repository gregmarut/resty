package com.gregmarut.resty.http.response;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class RestResponse
{
	private final int statusCode;
	private final Map<String, String> headers;
	private final byte[] data;
	
	public RestResponse(final int statusCode, final byte[] data, final Map<String, String> headers)
	{
		this.statusCode = statusCode;
		
		Map<String, String> h = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
		h.putAll(headers);
		
		this.headers = Collections.unmodifiableMap(h);
		this.data = data;
	}
	
	public byte[] getData()
	{
		return data;
	}
	
	public Map<String, String> getHeaders()
	{
		return headers;
	}
	
	public int getStatusCode()
	{
		return statusCode;
	}
}
