package com.gregmarut.resty.client.authentication.oauth2;

public class OAuthResponse
{
	public static final String ACCESS_TOKEN = "access_token";
	public static final String TOKEN_TYPE = "token_type";
	public static final String EXPIRES_IN = "expires_in";
	public static final String SCOPE = "scope";
	
	private String accessToken;
	private String tokenType;
	private long expiresIn;
	private String scope;
	
	/**
	 * @return the accessToken
	 */
	public String getAccessToken()
	{
		return accessToken;
	}
	
	/**
	 * @param accessToken
	 *        the accessToken to set
	 */
	public void setAccessToken(String accessToken)
	{
		this.accessToken = accessToken;
	}
	
	/**
	 * @return the tokenType
	 */
	public String getTokenType()
	{
		return tokenType;
	}
	
	/**
	 * @param tokenType
	 *        the tokenType to set
	 */
	public void setTokenType(String tokenType)
	{
		this.tokenType = tokenType;
	}
	
	/**
	 * @return the expiresIn
	 */
	public long getExpiresIn()
	{
		return expiresIn;
	}
	
	/**
	 * @param expiresIn
	 *        the expiresIn to set
	 */
	public void setExpiresIn(long expiresIn)
	{
		this.expiresIn = expiresIn;
	}
	
	/**
	 * @return the scope
	 */
	public String getScope()
	{
		return scope;
	}
	
	/**
	 * @param scope
	 *        the scope to set
	 */
	public void setScope(String scope)
	{
		this.scope = scope;
	}
}
