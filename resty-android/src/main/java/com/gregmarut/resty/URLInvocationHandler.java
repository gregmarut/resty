package com.gregmarut.resty;

import com.gregmarut.resty.exception.WebServiceException;
import com.gregmarut.resty.http.request.RestRequest;
import com.gregmarut.resty.http.response.RestResponse;
import com.gregmarut.resty.serialization.GsonSerializer;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

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
		try
		{
			//open a new url connection for this request
			HttpURLConnection httpURLConnection = openUrlConnection(request);
			
			final int responseCode = httpURLConnection.getResponseCode();
			final byte[] data = IOUtils.toByteArray(readStream(httpURLConnection, responseCode));
			
			//create a new rest response
			return new RestResponse(responseCode, data);
		}
		catch (IOException e)
		{
			throw new WebServiceException(e);
		}
	}
	
	private InputStream readStream(final HttpURLConnection httpURLConnection, final int responseCode) throws IOException
	{
		//check to see if the the response is less than the error codes
		if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST)
		{
			return httpURLConnection.getInputStream();
		}
		else
		{
			return httpURLConnection.getErrorStream();
		}
	}
	
	/**
	 * opens a new url connection
	 *
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private HttpURLConnection openUrlConnection(final RestRequest request) throws IOException
	{
		//create a new url object
		URL url = new URL(request.getUrl());
		
		//open a new url connection
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(request.getMethodType().toString());
		
		//for each of the headers
		for (Map.Entry<String, String> header : request.getHeaders().entrySet())
		{
			//apply the header to this url connection
			connection.setRequestProperty(header.getKey(), header.getValue());
		}
		
		//check to see if there is a data object to send
		if (null != request.getData())
		{
			//write the data to the output stream
			connection.setDoOutput(true);
			OutputStream os = connection.getOutputStream();
			IOUtils.write(request.getData(), os);
		}
		
		return connection;
	}
}
