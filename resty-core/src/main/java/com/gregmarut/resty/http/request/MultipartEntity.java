package com.gregmarut.resty.http.request;

import java.util.HashMap;
import java.util.Map;

public class MultipartEntity implements RequestEntity
{
	private final String fileName;
	private final byte[] contents;
	private final Map<String, String> params;
	
	public MultipartEntity(final String fileName, final byte[] contents)
	{
		this.fileName = fileName;
		this.contents = contents;
		params = new HashMap<>();
	}
	
	public String getFileName()
	{
		return fileName;
	}
	
	public byte[] getContents()
	{
		return contents;
	}
	
	public Map<String, String> getParams()
	{
		return params;
	}
}
