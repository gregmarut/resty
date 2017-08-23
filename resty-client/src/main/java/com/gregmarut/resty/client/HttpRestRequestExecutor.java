package com.gregmarut.resty.client;

import com.gregmarut.resty.RestRequestExecutor;
import com.gregmarut.resty.exception.UnexpectedResponseEntityException;
import com.gregmarut.resty.exception.WebServiceException;
import com.gregmarut.resty.http.request.RestRequest;
import com.gregmarut.resty.http.response.RestResponse;
import com.gregmarut.resty.http.response.RestResponseBuilder;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class HttpRestRequestExecutor implements RestRequestExecutor
{
	// holds the http client factory
	protected final HttpClientFactory httpClientFactory;
	
	public HttpRestRequestExecutor(final HttpClientFactory httpClientFactory)
	{
		this.httpClientFactory = httpClientFactory;
	}
	
	/**
	 * Executes the request on the http client
	 *
	 * @param restRequest
	 * @return
	 * @throws WebServiceException
	 */
	@Override
	public RestResponse executeRequest(final RestRequest restRequest) throws WebServiceException
	{
		try (CloseableHttpClient httpClient = httpClientFactory.createHttpClient())
		{
			//convert the rest request to an http uri request
			HttpUriRequest request = toHttpUriRequest(restRequest);
			
			// execute the call
			try (CloseableHttpResponse httpResponse = httpClient.execute(request))
			{
				// retrieve the http entity and status code
				final int statusCode = httpResponse.getStatusLine().getStatusCode();
				final HttpEntity httpEntity = httpResponse.getEntity();
				
				// holds the byte array of the response data from the webservice call
				byte[] data = null;
				
				// check to see if there is a response entity
				if (null != httpEntity)
				{
					try
					{
						// read the response and convert it to an object
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						httpEntity.writeTo(out);
						out.close();
						
						// set the response array
						data = out.toByteArray();
					}
					catch (IOException e)
					{
						throw new UnexpectedResponseEntityException(e);
					}
				}
				
				//create a new response builder
				RestResponseBuilder restResponseBuilder = RestResponseBuilder.create().setData(data).setStatusCode(statusCode);
				
				//make sure the headers are not null
				if (null != httpResponse.getAllHeaders())
				{
					//for each of the headers
					for (Header header : httpResponse.getAllHeaders())
					{
						restResponseBuilder.setHeader(header.getName(), header.getValue());
					}
				}
				
				return restResponseBuilder.build();
			}
		}
		catch (IOException e)
		{
			throw new WebServiceException(e);
		}
	}
	
	protected HttpUriRequest toHttpUriRequest(final RestRequest restRequest)
	{
		HttpUriRequest httpUriRequest;
		
		switch (restRequest.getMethodType())
		{
			case GET:
				httpUriRequest = new HttpGet(restRequest.getUrl());
				break;
			case POST:
				HttpPost httpPost = new HttpPost(restRequest.getUrl());
				//make sure the data is not null
				if (null != restRequest.getData())
				{
					httpPost.setEntity(new ByteArrayEntity(restRequest.getData()));
				}
				httpUriRequest = httpPost;
				break;
			case PUT:
				HttpPut httpPut = new HttpPut(restRequest.getUrl());
				//make sure the data is not null
				if (null != restRequest.getData())
				{
					httpPut.setEntity(new ByteArrayEntity(restRequest.getData()));
				}
				httpUriRequest = httpPut;
				break;
			case DELETE:
				httpUriRequest = new HttpDelete(restRequest.getUrl());
				break;
			default:
				throw new IllegalArgumentException("Unsupported method type: " + restRequest.getMethodType().toString());
		}
		
		//for each of the headers
		for (Map.Entry<String, String> header : restRequest.getHeaders().entrySet())
		{
			httpUriRequest.setHeader(header.getKey(), header.getValue());
		}
		
		return httpUriRequest;
	}
	
	public HttpClientFactory getHttpClientFactory()
	{
		return httpClientFactory;
	}
}
