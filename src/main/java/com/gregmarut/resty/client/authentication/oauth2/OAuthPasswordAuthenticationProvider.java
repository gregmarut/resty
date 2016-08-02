package com.gregmarut.resty.client.authentication.oauth2;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.gregmarut.resty.client.authentication.AuthenticationProvider;
import com.gregmarut.resty.client.authentication.basic.BasicAuthenticationProvider;

public class OAuthPasswordAuthenticationProvider implements AuthenticationProvider
{
	private static final Logger logger = LoggerFactory.getLogger(OAuthPasswordAuthenticationProvider.class);
	
	public static final String BEARER = "Bearer";
	public static final String ACCESS_TOKEN = "access_token";
	
	private final String username;
	private final String password;
	
	private final String clientIdentifier;
	private final String clientPassword;
	
	private final String tokenEndpoint;
	
	private String token;
	
	public OAuthPasswordAuthenticationProvider(final String username, String password, final String clientIdentifier,
		final String clientPassword, final String tokenEndpoint)
	{
		this.username = username;
		this.password = password;
		
		this.clientIdentifier = clientIdentifier;
		this.clientPassword = clientPassword;
		
		this.tokenEndpoint = tokenEndpoint;
	}
	
	@Override
	public void preRequest(final HttpUriRequest request)
	{
		// make sure the token is not null
		if (null != token)
		{
			// set the authorization header
			String bearerAuth = BEARER + " " + token;
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
				
				// retrieve the access token
				this.token = jsonElement.getAsJsonObject().get(ACCESS_TOKEN).getAsString();
				
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
}
