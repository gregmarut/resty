package com.gregmarut.resty.async;

import com.gregmarut.resty.exception.WebServiceException;

public class CompletableAsync<T> extends Async<T>
{
	public void completeSuccessful(final Object object)
	{
		if (null != onSuccess)
		{
			if (null != handler)
			{
				handler.accept(() -> onSuccess.accept((T) object));
			}
			else
			{
				onSuccess.accept((T) object);
			}
		}
		
		runFinally();
	}
	
	public void completeError(final WebServiceException e)
	{
		if (null != onError)
		{
			if (null != handler)
			{
				handler.accept(() -> onError.accept(e));
			}
			else
			{
				onError.accept(e);
			}
		}
		
		runFinally();
	}
	
	private void runFinally()
	{
		if (null != onFinally)
		{
			if (null != handler)
			{
				handler.accept(onFinally);
			}
			else
			{
				onFinally.run();
			}
		}
	}
}
