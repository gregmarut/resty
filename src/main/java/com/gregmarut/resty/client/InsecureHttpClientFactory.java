/*******************************************************************************
 * Copyright (c) 2014 Greg Marut. All rights reserved. This program and the accompanying materials
 * are made available
 * under the terms of the GNU Public License v3.0 which accompanies this distribution, and is
 * available at
 * http://www.gnu.org/licenses/gpl.html Contributors: Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.resty.client;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Disables the SSL verification for clients
 * 
 * @author Greg Marut
 */
public class InsecureHttpClientFactory extends HttpClientFactory
{
	@Override
	protected void addDefaultHttpParams(HttpClientBuilder clientBuilder)
	{
		super.addDefaultHttpParams(clientBuilder);
		
		// :FIXME:
	}
	
	private void disableSSLVerification(final DefaultHttpClient httpClient)
	{
		try
		{
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager()
			{
				
				public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException
				{
				}
				
				public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException
				{
				}
				
				public X509Certificate[] getAcceptedIssuers()
				{
					return null;
				}
			};
			
			X509HostnameVerifier verifier = new X509HostnameVerifier()
			{
				
				@Override
				public void verify(String string, SSLSocket ssls) throws IOException
				{
				}
				
				@Override
				public void verify(String string, X509Certificate xc) throws SSLException
				{
				}
				
				@Override
				public void verify(String string, String[] strings, String[] strings1) throws SSLException
				{
				}
				
				@Override
				public boolean verify(String string, SSLSession ssls)
				{
					return true;
				}
			};
			
			ctx.init(null, new TrustManager[]
			{
				tm
			}, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ssf.setHostnameVerifier(verifier);
			
			ClientConnectionManager ccm = httpClient.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", ssf, 443));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
