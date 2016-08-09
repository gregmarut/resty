package com.gregmarut.resty.client.authentication.oauth2;

import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

import com.gregmarut.resty.client.authentication.AuthenticationProvider;

public class OAuthTokenAuthenticationProvider implements AuthenticationProvider
{
	public static final String BEARER = "Bearer";
	
	private final String bearerAuth;
	
	public OAuthTokenAuthenticationProvider(final String token)
	{
		// make sure the token is not null
		if (null == token)
		{
			throw new IllegalArgumentException("token cannot be null");
		}
		
		this.bearerAuth = BEARER + " " + token;
	}
	
	@Override
	public void preRequest(final HttpUriRequest request)
	{
		// set the authorization header
		request.setHeader(HttpHeaders.AUTHORIZATION, bearerAuth);
	}
	
	@Override
	public boolean doAuthentication(final HttpClient httpClient)
	{
		// if it didnt work the first time, its not going to work now. There is nothing special is
		// needed for basic auth
		return false;
	}
}
