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
package com.gregmarut.resty.client.http;

public enum Scheme
{
	HTTP("http", 80),
	HTTPS("https", 443);
	
	private final String protocol;
	private final int port;
	
	private Scheme(final String protocol, final int port)
	{
		this.protocol = protocol;
		this.port = port;
	}
	
	public String getProtocol()
	{
		return protocol;
	}
	
	public int getPort()
	{
		return port;
	}
}
