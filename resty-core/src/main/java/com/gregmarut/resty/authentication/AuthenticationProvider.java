package com.gregmarut.resty.authentication;

import com.gregmarut.resty.RestRequestExecutor;
import com.gregmarut.resty.http.request.RestRequest;

public interface AuthenticationProvider
{
	/**
	 * Called before each request is made to the server
	 *
	 * @param request the request object
	 */
	void preRequest(RestRequest request);
	
	/**
	 * Allows the authentication provider to complete any necessary authentication steps and returns
	 * whether or not it was successful
	 *
	 * @return whether or not the authentication was successful
	 */
	boolean doAuthentication(RestRequestExecutor restRequestExecutor);
}
