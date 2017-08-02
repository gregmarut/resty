package com.gregmarut.resty.client;

import com.gregmarut.resty.DefaultStatusCodeHandler;
import com.gregmarut.resty.JSONInvocationHandler;
import com.gregmarut.resty.StatusCodeHandler;
import com.gregmarut.resty.authentication.AuthenticationProvider;
import com.gregmarut.resty.exception.UnexpectedResponseEntityException;
import com.gregmarut.resty.exception.WebServiceException;
import com.gregmarut.resty.http.request.RestRequest;
import com.gregmarut.resty.http.response.RestResponse;
import com.gregmarut.resty.http.response.RestResponseBuilder;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
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

public class HttpInvocationHandler extends JSONInvocationHandler<HttpClient>
{
	// holds the http client factory
	protected final HttpClientFactory httpClientFactory;
	
	public HttpInvocationHandler(final HttpClientFactory httpClientFactory, final String rootURL)
	{
		this(httpClientFactory, rootURL, new DefaultStatusCodeHandler());
	}
	
	public HttpInvocationHandler(final HttpClientFactory httpClientFactory, final String rootURL,
		final StatusCodeHandler statusCodeHandler)
	{
		super(rootURL, statusCodeHandler);
		this.httpClientFactory = httpClientFactory;
	}
	
	/**
	 * Executes the request on the http client
	 *
	 * @param restRequest
	 * @return
	 * @throws WebServiceException
	 */
	protected RestResponse executeRequest(final RestRequest restRequest) throws WebServiceException
	{
		try (CloseableHttpClient httpClient = httpClientFactory.createHttpClient())
		{
			return executeRequest(httpClient, restRequest, true);
		}
		catch (IOException e)
		{
			throw new WebServiceException(e);
		}
	}
	
	/**
	 * Executes the request on the http client
	 *
	 * @param httpClient
	 * @param restRequest
	 * @param allowAuthenticationAttempt if the request fails due to a 401 status code, this determines whether or not the authentication provider is
	 *                                   allowed to attempt authentication and retry the request
	 * @return
	 * @throws WebServiceException
	 */
	protected RestResponse executeRequest(final CloseableHttpClient httpClient, final RestRequest restRequest,
		final boolean allowAuthenticationAttempt) throws WebServiceException
	{
		try
		{
			final AuthenticationProvider<HttpClient> authenticationProvider = getAuthenticationProvider();
			
			// check to see if there is an authentication provider
			if (null != authenticationProvider)
			{
				// notify the authentication provider of the request before it is executed
				authenticationProvider.preRequest(restRequest);
			}
			
			//convert the rest request to an http uri request
			HttpUriRequest request = toHttpUriRequest(restRequest);
			
			// execute the call
			try (CloseableHttpResponse httpResponse = httpClient.execute(request))
			{
				// retrieve the http entity and status code
				final int statusCode = httpResponse.getStatusLine().getStatusCode();
				final HttpEntity httpEntity = httpResponse.getEntity();
				
				// check to see if there is an authentication provider and if response is an
				// unauthorized
				if (null != authenticationProvider && HttpStatus.SC_UNAUTHORIZED == statusCode)
				{
					// check to see if authentication is allowed
					if (allowAuthenticationAttempt)
					{
						// check to see if there is a www-authenticate header
						Header wwwAuthHeader = httpResponse.getFirstHeader(org.apache.http.HttpHeaders.WWW_AUTHENTICATE);
						if (null != wwwAuthHeader)
						{
							// let the authentication provider execute any authentication steps needed
							if (authenticationProvider.doAuthentication(httpClient))
							{
								// retry the request but do not allow authentication again if it fails a
								// second time
								return executeRequest(httpClient, restRequest, false);
							}
						}
					}
				}
				
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
