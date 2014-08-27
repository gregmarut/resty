/*******************************************************************************
 * Copyright (c) 2014 Greg Marut.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *    Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.resty.client;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;

/**
 * A factory that allows for the creation of various HttpRequests
 * 
 * @author Greg Marut
 */
public class RestRequestFactory
{
	public static final String CONTENT_TYPE_HEADER = "Content-Type";
	public static final String ACCEPT = "Accept";
	
	public static final String JSON_TYPE = "application/json";
	
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
	 * Create a new GET request
	 * 
	 * @param uri
	 * @return
	 */
	public HttpUriRequest createGetRequest(final String uri)
	{
		HttpGet httpGet = new HttpGet(uri);
		setHeaders(httpGet);
		
		return httpGet;
	}
	
	/**
	 * Create a new POST request
	 * 
	 * @param uri
	 * @param data
	 * @return
	 */
	public HttpUriRequest createPostRequest(final String uri, final byte[] data)
	{
		HttpPost httpPost = new HttpPost(uri);
		setHeaders(httpPost);
		
		ByteArrayEntity entity = new ByteArrayEntity(data);
		entity.setContentType(contentType);
		httpPost.setEntity(entity);
		
		return httpPost;
	}
	
	/**
	 * Create a new DELETE request
	 * 
	 * @param uri
	 * @return
	 */
	public HttpUriRequest createDeleteRequest(final String uri)
	{
		HttpDelete httpDelete = new HttpDelete(uri);
		setHeaders(httpDelete);
		
		return httpDelete;
	}
	
	/**
	 * Create a new PUT request
	 * 
	 * @param uri
	 * @param data
	 * @return
	 */
	public HttpUriRequest createPutRequest(final String uri, final byte[] data)
	{
		HttpPut httpPut = new HttpPut(uri);
		setHeaders(httpPut);
		
		ByteArrayEntity entity = new ByteArrayEntity(data);
		entity.setContentType(contentType);
		httpPut.setEntity(entity);
		
		return httpPut;
	}
	
	private void setHeaders(final HttpUriRequest request)
	{
		// set the headers
		request.setHeader(CONTENT_TYPE_HEADER, contentType);
		request.setHeader(ACCEPT, accept);
	}
}
