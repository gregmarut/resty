/*******************************************************************************
 * Copyright (c) 2014 Greg Marut.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * Contributors:
 * Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.resty;

import com.gregmarut.resty.http.request.RestRequest;

/**
 * A factory that allows for the creation of various HttpRequests
 *
 * @author Greg Marut
 */
public class RestRequestFactory
{
	public static final String CONTENT_TYPE_HEADER = "Content-Type";
	public static final String ACCEPT = "Accept";
	
	private final String contentType;
	private final String accept;
	
	public RestRequestFactory(final String type)
	{
		this.contentType = type;
		this.accept = type;
	}
	
	public RestRequestFactory(final String contentType, final String accept)
	{
		this.contentType = contentType;
		this.accept = accept;
	}
	
	/**
	 * Apply the default headers to the the rest request
	 *
	 * @param request
	 */
	protected void setDefaultHeaders(final RestRequest request)
	{
		// set the headers
		request.setHeader(CONTENT_TYPE_HEADER, contentType);
		request.setHeader(ACCEPT, accept);
	}
	
	/**
	 * Create a new GET request
	 *
	 * @param uri
	 * @return
	 */
	public RestRequest createGetRequest(final String uri)
	{
		RestRequest restRequest = new RestRequest(uri, MethodType.GET);
		setDefaultHeaders(restRequest);
		
		return restRequest;
	}
	
	/**
	 * Create a new POST request
	 *
	 * @param uri
	 * @param data
	 * @return
	 */
	public RestRequest createPostRequest(final String uri, final byte[] data)
	{
		RestRequest restRequest = new RestRequest(uri, MethodType.POST);
		restRequest.setData(data);
		setDefaultHeaders(restRequest);
		
		return restRequest;
	}
	
	/**
	 * Create a new DELETE request
	 *
	 * @param uri
	 * @return
	 */
	public RestRequest createDeleteRequest(final String uri)
	{
		RestRequest restRequest = new RestRequest(uri, MethodType.DELETE);
		setDefaultHeaders(restRequest);
		
		return restRequest;
	}
	
	/**
	 * Create a new PUT request
	 *
	 * @param uri
	 * @param data
	 * @return
	 */
	public RestRequest createPutRequest(final String uri, final byte[] data)
	{
		RestRequest restRequest = new RestRequest(uri, MethodType.PUT);
		restRequest.setData(data);
		setDefaultHeaders(restRequest);
		
		return restRequest;
	}
}
