package com.gregmarut.resty;

public class URLRestProxyFactory extends RestProxyFactory<URLInvocationHandler>
{
	public URLRestProxyFactory(final URLInvocationHandler restInvocationHandler)
	{
		super(restInvocationHandler);
	}
}
