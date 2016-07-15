package com.gregmarut.resty.client.authentication.basic;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;

import com.gregmarut.resty.client.authentication.AuthenticationProvider;

public class BasicAuthenticationProvider implements AuthenticationProvider
{
	public static final String BASIC = "Basic";
	
	private final String basicAuth;
	
	public BasicAuthenticationProvider(final String username, final String password)
	{
		String userpass = username + ":" + password;
		this.basicAuth = BASIC + " " + new String(new Base64().encode(userpass.getBytes()));
	}
	
	public void preRequest(final HttpRequest request)
	{
		// set the authorization header
		request.setHeader(HttpHeaders.AUTHORIZATION, basicAuth);
	}
	
	@Override
	public boolean doAuthentication(final HttpClient httpClient)
	{
		// if it didnt work the first time, its not going to work now. There is nothing special is
		// needed for basic auth
		return false;
	}
}
