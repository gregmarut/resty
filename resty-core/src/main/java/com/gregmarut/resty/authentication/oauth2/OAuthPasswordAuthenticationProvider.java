package com.gregmarut.resty.authentication.oauth2;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.gregmarut.resty.MethodType;
import com.gregmarut.resty.RestRequestExecutor;
import com.gregmarut.resty.authentication.AuthenticationProvider;
import com.gregmarut.resty.authentication.basic.BasicAuthenticationProvider;
import com.gregmarut.resty.exception.WebServiceException;
import com.gregmarut.resty.http.request.RestRequest;
import com.gregmarut.resty.http.response.RestResponse;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class OAuthPasswordAuthenticationProvider extends AuthenticationProvider
{
	private static final Logger logger = LoggerFactory.getLogger(OAuthPasswordAuthenticationProvider.class);
	
	public static final String DEFAULT_ENCODING = "UTF-8";
	public static final String AUTHORIZATION = "Authorization";
	public static final String BEARER = "Bearer";
	
	public static final int SC_OK = 200;
	
	private final String username;
	private final String password;
	
	private final String clientIdentifier;
	private final String clientPassword;
	
	private final String tokenEndpoint;
	
	private final AuthenticationListener listener;
	
	private OAuthResponse oauthResponse;
	
	public OAuthPasswordAuthenticationProvider(final String username, String password,
		final String clientIdentifier, final String clientPassword, final String tokenEndpoint)
	{
		this(username, password, clientIdentifier, clientPassword, tokenEndpoint, null);
	}
	
	public OAuthPasswordAuthenticationProvider(final String username, String password,
		final String clientIdentifier, final String clientPassword, final String tokenEndpoint, final AuthenticationListener listener)
	{
		this.username = username;
		this.password = password;
		
		this.clientIdentifier = clientIdentifier;
		this.clientPassword = clientPassword;
		
		this.tokenEndpoint = tokenEndpoint;
		
		this.listener = listener;
	}
	
	@Override
	public void preRequest(final RestRequest request)
	{
		// make sure the token is not null
		if (null != oauthResponse)
		{
			// set the authorization header
			String bearerAuth = BEARER + " " + oauthResponse.getAccessToken();
			request.setHeader(AUTHORIZATION, bearerAuth);
		}
	}
	
	@Override
	public boolean doAuthentication(final RestRequestExecutor restRequestExecutor)
	{
		// create a new http post request
		RestRequest post = new RestRequest(tokenEndpoint, MethodType.POST);
		
		if (null != clientIdentifier)
		{
			StringBuilder sb = new StringBuilder();
			sb.append(clientIdentifier);
			sb.append(":");
			
			if (null != clientPassword)
			{
				sb.append(clientPassword);
			}
			
			final String basicAuth = BasicAuthenticationProvider.BASIC + " " + new String(new Base64().encode(sb.toString().getBytes()));
			post.setHeader(AUTHORIZATION, basicAuth);
		}
		
		try
		{
			//put the post parameters
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("grant_type", "password");
			parameters.put("username", username);
			parameters.put("password", password);
			post.setFormEncodedData(parameters, DEFAULT_ENCODING);
			
			RestResponse restResponse = restRequestExecutor.executeRequest(post);
			
			// make sure this response was successful
			if (restResponse.getStatusCode() == SC_OK)
			{
				// read the response entity
				JsonElement jsonElement = new JsonParser().parse(new InputStreamReader(new ByteArrayInputStream(restResponse.getData())));
				JsonObject jsonObject = jsonElement.getAsJsonObject();
				
				// create an oauth response object
				OAuthResponse oauthResponse = new OAuthResponse();
				oauthResponse.setAccessToken(jsonObject.get(OAuthResponse.ACCESS_TOKEN).getAsString());
				oauthResponse.setTokenType(jsonObject.get(OAuthResponse.TOKEN_TYPE).getAsString());
				oauthResponse.setExpiresIn(jsonObject.get(OAuthResponse.EXPIRES_IN).getAsLong());
				oauthResponse.setScope(jsonObject.get(OAuthResponse.SCOPE).getAsString());
				
				// retrieve the access token
				this.oauthResponse = oauthResponse;
				
				// check to see if the listener is not null
				if (null != listener)
				{
					// notify the listener of an authentication
					listener.onAuthenticated(oauthResponse);
				}
				
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (WebServiceException | UnsupportedEncodingException | JsonSyntaxException e)
		{
			logger.info(e.getMessage(), e);
			return false;
		}
	}
	
	public interface AuthenticationListener
	{
		void onAuthenticated(OAuthResponse oauthResponse);
	}
}
