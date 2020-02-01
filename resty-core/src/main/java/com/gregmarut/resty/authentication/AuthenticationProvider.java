package com.gregmarut.resty.authentication;

import com.gregmarut.resty.RestRequestExecutor;
import com.gregmarut.resty.http.request.RestRequest;
import com.gregmarut.resty.http.response.RestResponse;

public abstract class AuthenticationProvider
{
	public static final int HTTP_UNAUTHORIZED = 401;
	
	/**
	 * Based on a given rest response, this checks to see if an attempt should be made to perform authentication
	 *
	 * @param restResponse
	 * @return
	 */
	public boolean shouldAttemptAuthentication(final RestResponse restResponse)
	{
		return HTTP_UNAUTHORIZED == restResponse.getStatusCode();
	}
	
	/**
	 * Called before each request is made to the server
	 *
	 * @param request the request object
	 */
	public abstract void preRequest(RestRequest request);
	
	/**
	 * Allows the authentication provider to complete any necessary authentication steps and returns
	 * whether or not it was successful
	 *
	 * @return whether or not the authentication was successful
	 */
	public abstract boolean doAuthentication(RestRequestExecutor restRequestExecutor);
}
