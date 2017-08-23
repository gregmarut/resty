package com.gregmarut.resty.authentication.oauth2;

import com.gregmarut.resty.RestRequestExecutor;
import com.gregmarut.resty.authentication.AuthenticationProvider;
import com.gregmarut.resty.http.request.RestRequest;

public class OAuthTokenAuthenticationProvider implements AuthenticationProvider
{
	public static final String AUTHORIZATION = "Authorization";
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
	public void preRequest(final RestRequest request)
	{
		// set the authorization header
		request.setHeader(AUTHORIZATION, bearerAuth);
	}
	
	@Override
	public boolean doAuthentication(final RestRequestExecutor restRequestExecutor)
	{
		// if it didnt work the first time, its not going to work now. There is nothing special is
		// needed for basic auth
		return false;
	}
}
