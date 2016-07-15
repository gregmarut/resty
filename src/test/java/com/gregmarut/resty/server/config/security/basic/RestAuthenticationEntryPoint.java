package com.gregmarut.resty.server.config.security.basic;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import com.google.gson.Gson;

public class RestAuthenticationEntryPoint extends BasicAuthenticationEntryPoint
{
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException)
		throws IOException, ServletException
	{
		// add the header to prompt the user for a login
		response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
		
		// create an error entity for the response
		ErrorEntity errorEntity = new ErrorEntity();
		errorEntity.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		errorEntity.setMessage(authException.getMessage());
		
		response.setStatus(HttpStatus.SC_UNAUTHORIZED);
		response.getOutputStream().write(new Gson().toJson(errorEntity).getBytes());
	}
}
