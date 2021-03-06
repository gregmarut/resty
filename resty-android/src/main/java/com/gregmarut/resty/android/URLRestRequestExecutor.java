package com.gregmarut.resty.android;

import com.gregmarut.resty.RestRequestExecutor;
import com.gregmarut.resty.exception.WebServiceException;
import com.gregmarut.resty.http.request.RestRequest;
import com.gregmarut.resty.http.response.RestResponse;
import com.gregmarut.resty.http.response.RestResponseBuilder;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class URLRestRequestExecutor implements RestRequestExecutor
{
	public static final int DEFAULT_TIMEOUT = 5000;
	
	private final int timeout;
	
	public URLRestRequestExecutor()
	{
		this(DEFAULT_TIMEOUT);
	}
	
	public URLRestRequestExecutor(final int timeout)
	{
		this.timeout = timeout;
		
		//check to see if the cookie handler has a default
		if (null == CookieHandler.getDefault())
		{
			//set the cookie manager
			CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
		}
	}
	
	@Override
	public RestResponse executeRequest(final RestRequest request) throws WebServiceException
	{
		try
		{
			//open a new url connection for this request
			HttpURLConnection httpURLConnection = openUrlConnection(request);
			
			final int responseCode = httpURLConnection.getResponseCode();
			final byte[] data = IOUtils.toByteArray(readStream(httpURLConnection, responseCode));
			
			//create a new response builder
			RestResponseBuilder restResponseBuilder = RestResponseBuilder.create().setData(data).setStatusCode(responseCode);
			
			//for each of the headers
			for (Map.Entry<String, List<String>> header : httpURLConnection.getHeaderFields().entrySet())
			{
				//make sure the key is not null
				if (null != header.getKey())
				{
					//:FIXME: only supporting the first value in the header list?
					String value = (null != header.getValue() && !header.getValue().isEmpty()) ? header.getValue().get(0) : null;
					
					restResponseBuilder.setHeader(header.getKey(), value);
				}
			}
			
			return restResponseBuilder.build();
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
		connection.setConnectTimeout(timeout);
		connection.setReadTimeout(timeout);
		
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
