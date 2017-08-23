package com.gregmarut.resty.android;

import com.gregmarut.resty.RestProxyFactory;
import com.gregmarut.resty.http.HostDetails;

public class URLRestProxyFactory extends RestProxyFactory<URLInvocationHandler>
{
	public URLRestProxyFactory(final String rootURL)
	{
		super(new URLInvocationHandler(rootURL));
	}
	
	public URLRestProxyFactory(final HostDetails hostDetails)
	{
		super(new URLInvocationHandler(hostDetails.getUrl()));
	}
	
	public URLRestProxyFactory(final URLInvocationHandler restInvocationHandler)
	{
		super(restInvocationHandler);
	}
}
