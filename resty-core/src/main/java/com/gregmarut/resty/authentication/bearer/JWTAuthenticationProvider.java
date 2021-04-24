package com.gregmarut.resty.authentication.bearer;

import com.gregmarut.resty.RestRequestExecutor;
import com.gregmarut.resty.authentication.AuthenticationProvider;
import com.gregmarut.resty.http.request.RestRequest;

public class JWTAuthenticationProvider extends AuthenticationProvider
{
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer";
	
	private String token;
	
	public void preRequest(final RestRequest request)
	{
		//make sure there is a token
		if (null != token)
		{
			// set the authorization header
			request.setHeader(AUTHORIZATION, BEARER + " " + token);
		}
	}
	
	@Override
	public boolean doAuthentication(final RestRequestExecutor restRequestExecutor)
	{
		// if it didnt work the first time, its not going to work now. There is nothing else we can do to authenticate
		return false;
	}
	
	public void setToken(final String token)
	{
		this.token = token;
	}
}