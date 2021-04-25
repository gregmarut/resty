package com.gregmarut.resty.client.integration;

import com.gregmarut.resty.android.URLRestProxyFactory;
import com.gregmarut.resty.http.HostDetails;
import com.gregmarut.resty.http.Scheme;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SimpleAsyncIT
{
	private final URLRestProxyFactory restProxyFactory;
	private final SimpleAsyncProxy simpleAsyncProxy;
	
	public SimpleAsyncIT()
	{
		// set up the rest proxy factory
		HostDetails hostDetails = new HostDetails(Scheme.HTTP, "localhost", null, 8080);
		restProxyFactory = new URLRestProxyFactory(hostDetails);
		simpleAsyncProxy = restProxyFactory.createProxy(SimpleAsyncProxy.class);
	}
	
	@Test
	public void getOneTest() throws ExecutionException, InterruptedException
	{
		CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
		simpleAsyncProxy.getOne()
			.onSuccess(completableFuture::complete)
			.onError(completableFuture::completeExceptionally);
		Assert.assertEquals(1, completableFuture.get().intValue());
	}
}
