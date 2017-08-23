package com.gregmarut.resty.android;

import com.gregmarut.resty.JSONInvocationHandler;
import com.gregmarut.resty.RestProxyFactory;
import com.gregmarut.resty.http.HostDetails;

public class URLRestProxyFactory extends RestProxyFactory<JSONInvocationHandler>
{
	public URLRestProxyFactory(final String rootURL)
	{
		super(new JSONInvocationHandler(rootURL, new URLRestRequestExecutor()));
	}
	
	public URLRestProxyFactory(final HostDetails hostDetails)
	{
		super(new JSONInvocationHandler(hostDetails.getUrl(), new URLRestRequestExecutor()));
	}
	
	public URLRestProxyFactory(final JSONInvocationHandler invocationHandler)
	{
		super(invocationHandler);
	}
}
