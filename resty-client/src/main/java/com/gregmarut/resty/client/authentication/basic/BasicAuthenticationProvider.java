package com.gregmarut.resty.client.authentication.basic;

import com.gregmarut.resty.authentication.AuthenticationProvider;
import com.gregmarut.resty.http.request.RestRequest;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;

public class BasicAuthenticationProvider implements AuthenticationProvider
{
	public static final String BASIC = "Basic";
	
	private final String basicAuth;
	
	public BasicAuthenticationProvider(final String username, final String password)
	{
		String userpass = username + ":" + password;
		this.basicAuth = BASIC + " " + new String(new Base64().encode(userpass.getBytes()));
	}
	
	public void preRequest(final RestRequest request)
	{
		// set the authorization header
		request.setHeader(HttpHeaders.AUTHORIZATION, basicAuth);
	}
	
	@Override
	public boolean doAuthentication()
	{
		// if it didnt work the first time, its not going to work now. There is nothing special is
		// needed for basic auth
		return false;
	}
}
