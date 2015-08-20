/*******************************************************************************
 * Copyright (c) 2014 Greg Marut. All rights reserved. This program and the accompanying materials are made available
 * under the terms of the GNU Public License v3.0 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html Contributors: Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.resty.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import com.gregmarut.resty.client.annotation.Expected;
import com.gregmarut.resty.client.annotation.HttpHeaders;
import com.gregmarut.resty.client.annotation.HttpParameters;
import com.gregmarut.resty.client.annotation.NameValue;
import com.gregmarut.resty.client.annotation.Parameter;
import com.gregmarut.resty.client.annotation.RestMethod;
import com.gregmarut.resty.client.exception.InvalidMethodTypeException;
import com.gregmarut.resty.client.exception.InvalidVariablePathException;
import com.gregmarut.resty.client.exception.MissingAnnotationException;
import com.gregmarut.resty.client.exception.UnexpectedEntityException;
import com.gregmarut.resty.client.exception.UnexpectedResponseEntityException;
import com.gregmarut.resty.client.exception.WebServiceException;
import com.gregmarut.resty.client.exception.status.StatusCodeException;
import com.gregmarut.resty.serialization.SerializationException;
import com.gregmarut.resty.serialization.Serializer;

/**
 * Handles methods invoked on proxy classes created for web service calls
 * 
 * @author Greg Marut
 */
public abstract class RestInvocationHandler implements InvocationHandler
{
	public static final String REGEX_VAR = "\\{([a-zA-Z0-9\\.]+?)\\}";
	public static final String REGEX_DOMAIN_URL = "^[a-zA-Z]+://([a-zA-Z0-9\\.\\-]+)";
	
	// holds the pattern object for capturing uri variables
	private final Pattern varPattern;
	private final Pattern domainPattern;
	
	// holds the http client factory
	protected final HttpClientFactory httpClientFactory;
	
	// holds the root URL
	protected final String rootURL;
	
	// holds the status code handler
	protected final StatusCodeHandler statusCodeHandler;
	
	// determines which class to use to hold the error entities
	private Class<?> errorClass;
	
	public RestInvocationHandler(final HttpClientFactory httpClientFactory, final String rootURL)
	{
		this(httpClientFactory, rootURL, new DefaultStatusCodeHandler());
	}
	
	public RestInvocationHandler(final HttpClientFactory httpClientFactory, final String rootURL,
			final StatusCodeHandler statusCodeHandler)
	{
		this.httpClientFactory = httpClientFactory;
		this.rootURL = rootURL;
		this.statusCodeHandler = statusCodeHandler;
		
		varPattern = Pattern.compile(REGEX_VAR);
		domainPattern = Pattern.compile(REGEX_DOMAIN_URL);
	}
	
	@Override
	public final Object invoke(final Object proxy, final Method method, final Object[] args) throws WebServiceException
	{
		// extract the rest method annotation
		RestMethod restMethod = method.getAnnotation(RestMethod.class);
		
		// extract the expected annotation
		Expected expected = method.getAnnotation(Expected.class);
		
		// make sure there is an annotation
		if (null == restMethod)
		{
			throw new MissingAnnotationException(method.getName() + " must be annotated with "
					+ RestMethod.class.getName());
		}
		
		// create a new parameter mapper
		ParameterMapper parameterMapper = new ParameterMapper(method, args);
		
		// build the uri
		String url = buildURL(parameterMapper, restMethod, args);
		
		// holds the http client to use
		HttpClient httpClient = null;
		
		try
		{
			// create a new http client
			httpClient = httpClientFactory.createHttpClient();
			
			// create the request
			HttpUriRequest request = createRequest(url, parameterMapper.getBodyArgument(), restMethod.method());
			
			// set the headers and parameters
			setHeadersAndParameters(method, request, parameterMapper);
			
			// execute the request
			HttpResponse response = executeRequest(httpClient, request);
			
			// determine the expected return type
			Class<?> expectedReturnType = method.getReturnType();
			
			// handle the response
			return handleResponse(response, expectedReturnType, expected);
		}
		finally
		{
			// make sure the client is not null
			if (null != httpClient && null != httpClient.getConnectionManager())
			{
				httpClient.getConnectionManager().shutdown();
			}
		}
	}
	
	/**
	 * Creates a request that will be executed by the client
	 * 
	 * @param url
	 * @param entity
	 * @param methodType
	 * @return
	 * @throws WebServiceException
	 */
	protected HttpUriRequest createRequest(final String url, final Object entity, final MethodType methodType)
			throws WebServiceException
	{
		// holds the http request
		HttpUriRequest request;
		
		// holds the entity data to send
		byte[] data;
		
		if (null != entity)
		{
			try
			{
				// serialize the data
				data = getSerializer().marshall(entity);
			}
			catch (SerializationException e)
			{
				throw new WebServiceException(e);
			}
		}
		else
		{
			data = null;
		}
		
		// switch based on the request type
		switch (methodType)
		{
			case GET:
				// make sure there is no entity
				if (null != entity)
				{
					throw new UnexpectedEntityException(
							"Entities are not allowed in GET requests. Mark any arguments with the "
									+ Parameter.class.getName() + " annotation.");
				}
				
				request = getRestRequestFactory().createGetRequest(url);
			break;
			
			case POST:
				request = getRestRequestFactory().createPostRequest(url, data);
			break;
			
			case PUT:
				request = getRestRequestFactory().createPutRequest(url, data);
			break;
			
			case DELETE:
				// make sure there is no entity
				if (null != entity)
				{
					throw new UnexpectedEntityException(
							"Entities are not allowed in DELETE requests. Mark any arguments with the "
									+ Parameter.class.getName() + " annotation.");
				}
				
				request = getRestRequestFactory().createDeleteRequest(url);
			break;
			
			default:
				throw new InvalidMethodTypeException();
		}
		
		return request;
	}
	
	/**
	 * Sets the headers and parameters if any are set
	 * 
	 * @param method
	 * @param request
	 */
	protected void setHeadersAndParameters(final Method method, final HttpUriRequest request,
			final ParameterMapper parameterMapper)
	{
		// retrieve the list of http headers from the factory
		Set<Entry<String, String>> httpHeaderEntries = httpClientFactory.getHttpHeaders().entrySet();
		
		// make sure there are values in the set
		if (null != httpHeaderEntries && !httpHeaderEntries.isEmpty())
		{
			// for each entry in the set
			for (Entry<String, String> entry : httpHeaderEntries)
			{
				// set the header
				request.setHeader(entry.getKey(), entry.getValue());
			}
		}
		
		// retrieve the header and parameter annotations
		HttpHeaders httpHeaders = method.getAnnotation(HttpHeaders.class);
		HttpParameters httpParameters = method.getAnnotation(HttpParameters.class);
		
		// check to see if the headers is not null
		if (null != httpHeaders)
		{
			// retrieve the array of name value pairs
			NameValue[] nameValues = httpHeaders.value();
			
			// make sure the name value pairs are not null
			if (null != nameValues)
			{
				// for each of the name values
				for (NameValue nameValue : nameValues)
				{
					// make sure this name value is not null
					if (null != nameValue)
					{
						// set the header
						request.setHeader(nameValue.name(), replaceVariables(nameValue.value(), parameterMapper));
					}
				}
			}
		}
		
		// check to see if the parameters is not null
		if (null != httpParameters)
		{
			// retrieve the array of name value pairs
			NameValue[] nameValues = httpParameters.value();
			
			// make sure the name value pairs are not null
			if (null != nameValues)
			{
				// for each of the name values
				for (NameValue nameValue : nameValues)
				{
					// make sure this name value is not null
					if (null != nameValue)
					{
						// retrieve the parameters from the request
						HttpParams params = request.getParams();
						
						// check to see if the params is null
						if (null == params)
						{
							// create a new params object
							params = new BasicHttpParams();
							request.setParams(params);
						}
						
						// set the parameter
						params.setParameter(nameValue.name(), replaceVariables(nameValue.value(), parameterMapper));
					}
				}
			}
		}
	}
	
	/**
	 * Executes the request on the http client
	 * 
	 * @param httpClient
	 * @param request
	 * @return
	 * @throws WebServiceException
	 */
	protected HttpResponse executeRequest(final HttpClient httpClient, final HttpUriRequest request)
			throws WebServiceException
	{
		try
		{
			// execute the call
			return httpClient.execute(request);
		}
		catch (IOException e)
		{
			throw new WebServiceException(e);
		}
	}
	
	/**
	 * Handles the response that is returned from the http client
	 * 
	 * @param httpResponse
	 * @param expectedReturnType
	 * @return
	 * @throws StatusCodeException
	 * @throws UnexpectedResponseEntityException
	 */
	protected Object handleResponse(final HttpResponse httpResponse, final Class<?> expectedReturnType,
			final Expected expected) throws WebServiceException, UnexpectedResponseEntityException
	{
		Object result;
		Object errorResult = null;
		
		// retrieve the http entity
		HttpEntity httpEntity = httpResponse.getEntity();
		
		// holds the status code
		final int statusCode = httpResponse.getStatusLine().getStatusCode();
		
		try
		{
			// holds the byte array of the response from the webservice call
			byte[] response = null;
			
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
					response = out.toByteArray();
				}
				catch (IOException e)
				{
					throw new UnexpectedResponseEntityException(e);
				}
			}
			
			// check to see if there is a response
			if (null != response)
			{
				// check to see if there is a return type
				if (null != expectedReturnType && !expectedReturnType.equals(void.class))
				{
					// check to see if the return type is a byte array
					if (byte[].class.equals(expectedReturnType))
					{
						result = response;
					}
					else if (String.class.equals(expectedReturnType))
					{
						result = new String(response);
					}
					else
					{
						try
						{
							// deserialize the result
							result = getSerializer().unmarshall(response, expectedReturnType);
						}
						catch (SerializationException e)
						{
							// create the error that will be reported
							UnexpectedResponseEntityException exception = new UnexpectedResponseEntityException(e);
							
							// make sure the error class is not null
							if (null != errorClass)
							{
								try
								{
									// attempt to deserialze the error message
									exception.setErrorEntity(getSerializer().unmarshall(response, errorClass));
									throw exception;
								}
								catch (SerializationException e1)
								{
									throw exception;
								}
							}
							else
							{
								throw exception;
							}
						}
					}
				}
				else
				{
					result = null;
					
					// make sure the error class is not null
					if (null != errorClass)
					{
						try
						{
							// attempt to deserialze the error message
							errorResult = getSerializer().unmarshall(response, errorClass);
						}
						catch (SerializationException e)
						{
							// ignore this error
						}
					}
				}
			}
			else
			{
				// there is no result
				result = null;
			}
			
			// get the expected status code
			int expectedStatusCode = (null != expected ? expected.statusCode() : Expected.DEFAULT_STATUS_CODE);
			
			// check to see if the expected status code is set
			if (expectedStatusCode != Expected.DEFAULT_STATUS_CODE)
			{
				// allow the subclass to handle the status code
				statusCodeHandler.handleStatusCode(statusCode, expectedStatusCode, errorResult);
			}
			else
			{
				// allow the subclass to handle the status code
				statusCodeHandler.handleStatusCode(statusCode, errorResult);
			}
		}
		finally
		{
			try
			{
				InputStream is = httpEntity.getContent();
				
				// make sure the input stream is not null
				if (null != is)
				{
					is.close();
				}
			}
			catch (IllegalStateException e)
			{
				// ignore
			}
			catch (IOException e)
			{
				// ignore
			}
		}
		
		// return the result
		return result;
	}
	
	/**
	 * Returns the factory that is responsible for creating rest request objects
	 * 
	 * @return
	 */
	protected abstract RestRequestFactory getRestRequestFactory();
	
	/**
	 * Returns the serializer that is responsible for marshalling and unmarshalling data
	 * 
	 * @return
	 */
	protected abstract Serializer getSerializer();
	
	/**
	 * Builds the URL based on the
	 * 
	 * @param parameterMapper
	 * @param restMethod
	 * @param args
	 * @return
	 */
	private String buildURL(final ParameterMapper parameterMapper, final RestMethod restMethod, final Object[] args)
	{
		// replace the variables in the uri
		String uri = replaceVariables(restMethod.uri(), parameterMapper);
		
		// check to see if the domain is already there
		if (null != rootURL && !domainPattern.matcher(uri).find())
		{
			// build the url
			StringBuilder url = new StringBuilder();
			url.append(rootURL);
			url.append(uri);
			
			return url.toString();
		}
		else
		{
			return uri;
		}
	}
	
	private String replaceVariables(final String original, final ParameterMapper parameterMapper)
	{
		// create a matcher that finds any variables in the string
		Matcher matcher = varPattern.matcher(original);
		
		String string = original;
		
		// while there are more matches
		while (matcher.find())
		{
			// get the current variable
			String var = matcher.group();
			
			// strip the start and end tags
			String varName = var.substring(1, var.length() - 1);
			
			// resolve the variable value
			String value = resolveObjectVariable(parameterMapper, varName);
			
			// replace the variable
			string = string.replace(var, value);
		}
		
		return string;
	}
	
	private String resolveObjectVariable(final ParameterMapper parameterMapper, final String varName)
	{
		// tokenize the string based on the object deliminiter
		StringTokenizer tokenizer = new StringTokenizer(varName, ".");
		
		// get the first parameter
		Object next = parameterMapper.getArgument(tokenizer.nextToken());
		
		// while there are more tokens
		while (tokenizer.hasMoreTokens())
		{
			try
			{
				Field field = next.getClass().getField(tokenizer.nextToken());
				field.setAccessible(true);
				next = field.get(next);
			}
			catch (NoSuchFieldException e)
			{
				throw new InvalidVariablePathException("No parameter could be resolved with the path: " + varName);
			}
			catch (IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		try
		{
			return URLEncoder.encode(next.toString(), "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			return next.toString();
		}
	}
	
	public Class<?> getErrorClass()
	{
		return errorClass;
	}
	
	public void setErrorClass(Class<?> errorClass)
	{
		this.errorClass = errorClass;
	}
	
	public HttpClientFactory getHttpClientFactory()
	{
		return httpClientFactory;
	}
}
