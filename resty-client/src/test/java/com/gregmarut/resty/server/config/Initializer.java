package com.gregmarut.resty.server.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class Initializer extends AbstractAnnotationConfigDispatcherServletInitializer
{
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException
	{
		super.onStartup(servletContext);
	}
	
	@Override
	protected Class<?>[] getRootConfigClasses()
	{
		return new Class[]
		{
			AppConfig.class
		};
	}
	
	@Override
	protected Class<?>[] getServletConfigClasses()
	{
		return new Class[]
		{
			ServletConfig.class
		};
	}
	
	@Override
	protected String[] getServletMappings()
	{
		return new String[]
		{
			"/"
		};
	}
}