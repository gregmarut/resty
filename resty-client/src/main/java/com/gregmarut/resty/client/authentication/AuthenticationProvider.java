package com.gregmarut.resty.client.authentication;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

public interface AuthenticationProvider
{
	/**
	 * Called before each request is made to the server
	 * 
	 * @param request
	 *        the request object
	 */
	void preRequest(HttpUriRequest request);
	
	/**
	 * Allows the authentication provider to complete any necessary authentication steps and returns
	 * whether or not it was successful
	 * 
	 * @return whether or not the authentication was successful
	 */
	boolean doAuthentication(HttpClient httpClient);
}
