package com.gregmarut.resty.http.request;

public class ByteArrayEntity implements RequestEntity
{
	private final byte[] bytes;
	
	public ByteArrayEntity(final byte[] bytes)
	{
		this.bytes = bytes;
	}
	
	public byte[] getBytes()
	{
		return bytes;
	}
}
