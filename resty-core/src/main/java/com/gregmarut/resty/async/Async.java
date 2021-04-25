package com.gregmarut.resty.async;

import com.gregmarut.resty.exception.WebServiceException;

import java.util.function.Consumer;

public class Async<T>
{
	protected Consumer<T> onSuccess;
	protected Consumer<WebServiceException> onError;
	protected Runnable onFinally;
	
	public Async<T> onSuccess(final Consumer<T> onSuccess)
	{
		this.onSuccess = onSuccess;
		return this;
	}
	
	public Async<T> onError(final Consumer<WebServiceException> onError)
	{
		this.onError = onError;
		return this;
	}
	
	public Async<T> onFinally(final Runnable onFinally)
	{
		this.onFinally = onFinally;
		return this;
	}
}
