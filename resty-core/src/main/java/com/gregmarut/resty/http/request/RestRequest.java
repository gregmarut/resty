package com.gregmarut.resty.http.request;

import com.gregmarut.resty.MethodType;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RestRequest
{
	private final String url;
	private final MethodType methodType;
	private final Map<String, String> headers;
	private final Map<String, String> parameters;
	private RequestEntity requestEntity;
	
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
	
	public RequestEntity getRequestEntity()
	{
		return requestEntity;
	}
	
	public void setRequestEntity(final RequestEntity requestEntity)
	{
		this.requestEntity = requestEntity;
	}
	
	public void setFormEncodedData(final Map<String, String> parameters, final String encoding) throws UnsupportedEncodingException
	{
		StringBuilder sb = new StringBuilder();
		
		//for each of the parameters
		for (Map.Entry<String, String> param : parameters.entrySet())
		{
			if (sb.length() > 0)
			{
				sb.append("&");
			}
			
			//encode the key and value
			String key = URLEncoder.encode(param.getKey(), encoding);
			String value = URLEncoder.encode(param.getValue(), encoding);
			
			sb.append(key);
			sb.append("=");
			sb.append(value);
		}
		
		setRequestEntity(new ByteArrayEntity(sb.toString().getBytes()));
	}
}
