/*******************************************************************************
 * Copyright (c) 2014 Greg Marut. All rights reserved. This program and the accompanying materials
 * are made available
 * under the terms of the GNU Public License v3.0 which accompanies this distribution, and is
 * available at
 * http://www.gnu.org/licenses/gpl.html Contributors: Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.resty;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.gregmarut.resty.annotation.Expected;
import com.gregmarut.resty.annotation.HttpHeaders;
import com.gregmarut.resty.annotation.HttpParameters;
import com.gregmarut.resty.annotation.NameValue;
import com.gregmarut.resty.annotation.Parameter;
import com.gregmarut.resty.annotation.RestMethod;
import com.gregmarut.resty.async.CompletableAsync;
import com.gregmarut.resty.authentication.AuthenticationProvider;
import com.gregmarut.resty.exception.InvalidMethodTypeException;
import com.gregmarut.resty.exception.InvalidVariablePathException;
import com.gregmarut.resty.exception.MissingAnnotationException;
import com.gregmarut.resty.exception.UnexpectedEntityException;
import com.gregmarut.resty.exception.UnexpectedResponseEntityException;
import com.gregmarut.resty.exception.WebServiceException;
import com.gregmarut.resty.exception.status.StatusCodeException;
import com.gregmarut.resty.http.request.RestRequest;
import com.gregmarut.resty.http.response.RestResponse;
import com.gregmarut.resty.util.ReturnType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles methods invoked on proxy classes created for web service calls
 *
 * @author Greg Marut
 */
public abstract class RestInvocationHandler implements InvocationHandler
{
	private static final Logger logger = LoggerFactory.getLogger(RestInvocationHandler.class);
	
	public static final String REGEX_VAR = "\\{([a-zA-Z0-9\\.]+?)\\}";
	public static final String REGEX_DOMAIN_URL = "^[a-zA-Z]+://([a-zA-Z0-9\\.\\-]+)";
	
	private final Gson gson;
	
	private final RestRequestExecutor restRequestExecutor;
	
	//holds the executor service to run async calls
	private ExecutorService asyncExecutorService;
	
	// holds the pattern object for capturing uri variables
	private final Pattern varPattern;
	private final Pattern domainPattern;
	
	// holds the root URL
	protected final String rootURL;
	
	// holds the status code handler
	protected final StatusCodeHandler statusCodeHandler;
	
	// holds the authentication provider
	private AuthenticationProvider authenticationProvider;
	
	// determines which class to use to hold the error entities
	private Class<?> errorClass;
	
	public RestInvocationHandler(final String rootURL, final RestRequestExecutor restRequestExecutor)
	{
		this(rootURL, restRequestExecutor, new DefaultStatusCodeHandler());
	}
	
	public RestInvocationHandler(final String rootURL, final RestRequestExecutor restRequestExecutor,
		final StatusCodeHandler statusCodeHandler)
	{
		if (null == rootURL)
		{
			throw new IllegalArgumentException("rootURL cannot be null");
		}
		
		if (null == restRequestExecutor)
		{
			throw new IllegalArgumentException("restRequestExecutor cannot be null");
		}
		
		if (null == statusCodeHandler)
		{
			throw new IllegalArgumentException("statusCodeHandler cannot be null");
		}
		
		this.rootURL = rootURL;
		this.restRequestExecutor = restRequestExecutor;
		this.statusCodeHandler = statusCodeHandler;
		this.gson = new Gson();
		
		//set a default async executor service
		setAsyncExecutorService(Executors.newFixedThreadPool(4));
		
		varPattern = Pattern.compile(REGEX_VAR);
		domainPattern = Pattern.compile(REGEX_DOMAIN_URL);
	}
	
	@Override
	public final Object invoke(final Object proxy, final Method method, final Object[] args) throws WebServiceException
	{
		// determine the expected return type
		final ReturnType methodReturnType = new ReturnType(method);
		
		//check to see if the expected return type is an async object
		if (methodReturnType.isAsync())
		{
			final CompletableAsync<?> async = new CompletableAsync<>();
			final ReturnType genericReturnType = methodReturnType.getGenericReturnType().orElseGet(() -> new ReturnType(Object.class));
			
			asyncExecutorService.execute(() -> {
				try
				{
					async.completeSuccessful(invoke(method, args, genericReturnType));
				}
				catch (WebServiceException e)
				{
					async.completeError(e);
				}
			});
			
			return async;
		}
		else
		{
			// run synchronously
			return invoke(method, args, methodReturnType);
		}
	}
	
	private Object invoke(final Method method, final Object[] args, final ReturnType returnType) throws WebServiceException
	{
		//check to see if this method is being invoked on the core Object class which are not expected to be annotated and are not rest method
		if (method.getDeclaringClass().equals(Object.class))
		{
			try
			{
				return method.invoke(method, args);
			}
			catch (InvocationTargetException | IllegalAccessException e)
			{
				return null;
			}
		}
		
		// extract the rest method annotation
		final RestMethod restMethod = method.getAnnotation(RestMethod.class);
		
		// extract the expected annotation
		final Expected expected = method.getAnnotation(Expected.class);
		
		// make sure there is an annotation
		if (null == restMethod)
		{
			throw new MissingAnnotationException(method.getName() + " must be annotated with "
				+ RestMethod.class.getName());
		}
		
		// create a new parameter mapper
		final ParameterMapper parameterMapper = new ParameterMapper(method, args);
		
		// build the uri
		final String url = buildURL(parameterMapper, restMethod, args);
		
		// create the request
		final RestRequest request = createRequest(url, parameterMapper.getBodyArgument(), restMethod.method());
		
		// set the headers and parameters
		setHeadersAndParameters(method, request, parameterMapper);
		
		// execute the request
		final RestResponse response = executeRequest(request, true);
		
		// handle the response
		return handleResponse(response, returnType, expected);
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
	protected RestRequest createRequest(final String url, final Object entity, final MethodType methodType)
		throws WebServiceException
	{
		// holds the http request
		RestRequest request;
		
		// holds the entity data to send
		byte[] data;
		
		if (null != entity)
		{
			try
			{
				// serialize the data
				data = gson.toJson(entity).getBytes();
			}
			catch (JsonSyntaxException e)
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
	protected void setHeadersAndParameters(final Method method, final RestRequest request,
		final ParameterMapper parameterMapper)
	{
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
						// set the parameter
						request.setParameter(nameValue.name(), replaceVariables(nameValue.value(), parameterMapper));
					}
				}
			}
		}
	}
	
	private RestResponse executeRequest(final RestRequest request, final boolean allowAuthenticationAttempt) throws WebServiceException
	{
		// check to see if there is an authentication provider
		if (null != authenticationProvider)
		{
			// notify the authentication provider of the request before it is executed
			authenticationProvider.preRequest(request);
		}
		
		//execute the request to the server
		RestResponse restResponse = restRequestExecutor.executeRequest(request);
		
		// check to see if an authentication attempt is even allowed, there is an authentication provider and if that provider thinks we should
		// attempt toauthenticate
		if (allowAuthenticationAttempt && null != authenticationProvider && authenticationProvider.shouldAttemptAuthentication(restResponse))
		{
			// let the authentication provider execute any authentication steps needed
			if (authenticationProvider.doAuthentication(restRequestExecutor))
			{
				// retry the request but do not allow authentication again if it fails a
				// second time
				return executeRequest(request, false);
			}
		}
		
		return restResponse;
	}
	
	/**
	 * Handles the response that is returned from the http client
	 *
	 * @param restResponse
	 * @param returnType
	 * @return
	 * @throws StatusCodeException
	 * @throws WebServiceException
	 */
	protected Object handleResponse(final RestResponse restResponse, final ReturnType returnType,
		final Expected expected) throws WebServiceException
	{
		Object result;
		Object errorResult = null;
		
		// holds the status code
		final int statusCode = restResponse.getStatusCode();
		
		// holds the byte array of the response from the webservice call
		byte[] response = restResponse.getData();
		
		// get the expected status code
		final int expectedStatusCode = (null != expected ? expected.statusCode() : Expected.DEFAULT_STATUS_CODE);
		
		// check to see if there is a response
		if (null != response)
		{
			// check to see if there is a return type
			if (!returnType.getActualClass().equals(void.class))
			{
				// using the status codes, check to see if this request was successful
				if (statusCodeHandler.isSuccessful(statusCode, expectedStatusCode))
				{
					// check to see if the return type is a byte array
					if (byte[].class.equals(returnType.getActualClass()))
					{
						result = response;
					}
					else if (String.class.equals(returnType.getActualClass()))
					{
						result = new String(response);
					}
					else if (returnType.isList())
					{
						try
						{
							final Class<?> genericType = returnType.getGenericReturnType()
								.orElseGet(() -> new ReturnType(Object.class))
								.getActualClass();
							
							final List<Object> results = new ArrayList<>();
							final JsonArray jsonArray = JsonParser.parseString(new String(response)).getAsJsonArray();
							
							jsonArray.forEach(jsonElement -> results.add(gson.fromJson(jsonElement, genericType)));
							
							result = results;
						}
						catch (JsonSyntaxException e)
						{
							// create the error that will be reported
							throw new UnexpectedResponseEntityException(e);
						}
					}
					else
					{
						try
						{
							// deserialize the result
							result = gson.fromJson(new String(response), returnType.getActualClass());
						}
						catch (JsonSyntaxException e)
						{
							// create the error that will be reported
							throw new UnexpectedResponseEntityException(e);
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
							errorResult = gson.fromJson(new String(response), errorClass);
						}
						catch (JsonSyntaxException e)
						{
							logger.warn(e.getMessage(), e);
							logger.warn(new String(response));
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
						errorResult = gson.fromJson(new String(response), errorClass);
					}
					catch (JsonSyntaxException e)
					{
						logger.warn(e.getMessage(), e);
						logger.warn(new String(response));
					}
				}
			}
		}
		else
		{
			// there is no result
			result = null;
		}
		
		// allow the subclass to handle the status code
		statusCodeHandler.verifyStatusCode(statusCode, expectedStatusCode, errorResult);
		
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
	
	public AuthenticationProvider getAuthenticationProvider()
	{
		return authenticationProvider;
	}
	
	public void setAuthenticationProvider(final AuthenticationProvider authenticationProvider)
	{
		this.authenticationProvider = authenticationProvider;
	}
	
	public ExecutorService getAsyncExecutorService()
	{
		return asyncExecutorService;
	}
	
	public void setAsyncExecutorService(final ExecutorService asyncExecutorService)
	{
		this.asyncExecutorService = asyncExecutorService;
	}
	
	public RestRequestExecutor getRestRequestExecutor()
	{
		return restRequestExecutor;
	}
}
