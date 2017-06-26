package com.gregmarut.resty;

import com.gregmarut.resty.exception.WebServiceException;
import com.gregmarut.resty.http.request.RestRequest;
import com.gregmarut.resty.http.response.RestResponse;
import com.gregmarut.resty.serialization.GsonSerializer;

public class URLInvocationHandler extends JSONInvocationHandler
{
	public URLInvocationHandler(final String rootURL)
	{
		super(rootURL);
	}
	
	public URLInvocationHandler(final String rootURL, final StatusCodeHandler statusCodeHandler)
	{
		super(rootURL, statusCodeHandler);
	}
	
	public URLInvocationHandler(final String rootURL, final StatusCodeHandler statusCodeHandler,
		final GsonSerializer serializer)
	{
		super(rootURL, statusCodeHandler, serializer);
	}
	
	@Override
	protected RestResponse executeRequest(final RestRequest request) throws WebServiceException
	{
		return null;
	}
}
