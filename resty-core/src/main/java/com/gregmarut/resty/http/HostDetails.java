/*******************************************************************************
 * Copyright (c) 2014 Greg Marut.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *    Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.resty.http;

public class HostDetails
{
	private final Scheme scheme;
	private final String domain;
	private final String path;
	private final int port;
	
	private final String url;
	
	public HostDetails(final Scheme scheme, final String domain)
	{
		this(scheme, domain, null, scheme.getPort());
	}
	
	public HostDetails(final Scheme scheme, final String domain, final String path, final int port)
	{
		this.scheme = scheme;
		this.domain = domain;
		this.path = path;
		this.port = port;
		
		//build the url;
		StringBuilder sb = new StringBuilder();
		sb.append(scheme.getProtocol());
		sb.append("://");
		sb.append(domain);
		
		//check to see if the port is anything but the default
		if(scheme.getPort() != port)
		{
			sb.append(":");
			sb.append(port);
		}
		
		//make sure the path is not null
		if(null != path)
		{
			sb.append(path);
		}
		
		url = sb.toString();
	}
	
	public Scheme getScheme()
	{
		return scheme;
	}

	public String getDomain()
	{
		return domain;
	}

	public String getPath()
	{
		return path;
	}

	public int getPort()
	{
		return port;
	}

	public String getUrl()
	{
		return url;
	}
}
