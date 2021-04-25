package com.gregmarut.resty.async;

import com.gregmarut.resty.exception.WebServiceException;

public class CompletableAsync<T> extends Async<T>
{
	public void completeSuccessful(final Object object)
	{
		if (null != onSuccess)
		{
			onSuccess.accept((T) object);
		}
		
		runFinally();
	}
	
	public void completeError(final WebServiceException e)
	{
		if (null != onError)
		{
			onError.accept(e);
		}
		
		runFinally();
	}
	
	private void runFinally()
	{
		if (null != onFinally)
		{
			onFinally.run();
		}
	}
}
