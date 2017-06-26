package com.gregmarut.resty.http.response;

public class RestResponse
{
	public final int statusCode;
	public final byte[] data;
	
	public RestResponse(final int statusCode, final byte[] data)
	{
		this.statusCode = statusCode;
		this.data = data;
	}
	
	public byte[] getData()
	{
		return data;
	}
	
	public int getStatusCode()
	{
		return statusCode;
	}
}
