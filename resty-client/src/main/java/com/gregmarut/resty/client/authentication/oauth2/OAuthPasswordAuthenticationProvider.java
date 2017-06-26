package com.gregmarut.resty.client.authentication.oauth2;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.gregmarut.resty.client.authentication.AuthenticationProvider;
import com.gregmarut.resty.client.authentication.basic.BasicAuthenticationProvider;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class OAuthPasswordAuthenticationProvider implements AuthenticationProvider
{
	private static final Logger logger = LoggerFactory.getLogger(OAuthPasswordAuthenticationProvider.class);
	
	public static final String BEARER = "Bearer";
	
	private final String username;
	private final String password;
	
	private final String clientIdentifier;
	private final String clientPassword;
	
	private final String tokenEndpoint;
	
	private final AuthenticationListener listener;
	
	private OAuthResponse oauthResponse;
	
	public OAuthPasswordAuthenticationProvider(final String username, String password, final String clientIdentifier,
		final String clientPassword, final String tokenEndpoint)
	{
		this(username, password, clientIdentifier, clientPassword, tokenEndpoint, null);
	}
	
	public OAuthPasswordAuthenticationProvider(final String username, String password, final String clientIdentifier,
		final String clientPassword, final String tokenEndpoint, final AuthenticationListener listener)
	{
		this.username = username;
		this.password = password;
		
		this.clientIdentifier = clientIdentifier;
		this.clientPassword = clientPassword;
		
		this.tokenEndpoint = tokenEndpoint;
		
		this.listener = listener;
	}
	
	@Override
	public void preRequest(final HttpUriRequest request)
	{
		// make sure the token is not null
		if (null != oauthResponse)
		{
			// set the authorization header
			String bearerAuth = BEARER + " " + oauthResponse.getAccessToken();
			request.setHeader(HttpHeaders.AUTHORIZATION, bearerAuth);
		}
	}
	
	@Override
	public boolean doAuthentication(final HttpClient httpClient)
	{
		// create a new http post request
		HttpPost post = new HttpPost(tokenEndpoint);
		
		if (null != clientIdentifier)
		{
			StringBuilder sb = new StringBuilder();
			sb.append(clientIdentifier);
			sb.append(":");
			
			if (null != clientPassword)
			{
				sb.append(clientPassword);
			}
			
			final String basicAuth =
				BasicAuthenticationProvider.BASIC + " " + new String(new Base64().encode(sb.toString().getBytes()));
			post.setHeader(HttpHeaders.AUTHORIZATION, basicAuth);
		}
		
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("grant_type", "password"));
		urlParameters.add(new BasicNameValuePair("username", username));
		urlParameters.add(new BasicNameValuePair("password", password));
		
		try
		{
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			
			// execute the request
			HttpResponse response = httpClient.execute(post);
			
			// make sure this response was successful
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				// read the response entity
				InputStream entity = response.getEntity().getContent();
				JsonElement jsonElement = new JsonParser().parse(new InputStreamReader(entity));
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
		catch (IOException | JsonSyntaxException e)
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