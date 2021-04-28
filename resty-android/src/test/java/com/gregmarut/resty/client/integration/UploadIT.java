package com.gregmarut.resty.client.integration;

import com.gregmarut.resty.android.URLRestProxyFactory;
import com.gregmarut.resty.exception.WebServiceException;
import com.gregmarut.resty.http.HostDetails;
import com.gregmarut.resty.http.Scheme;
import com.gregmarut.resty.http.request.MultipartEntity;
import org.junit.Assert;
import org.junit.Test;

public class UploadIT
{
	private final URLRestProxyFactory restProxyFactory;
	private final UploadProxy uploadProxy;
	
	public UploadIT()
	{
		// set up the rest proxy factory
		HostDetails hostDetails = new HostDetails(Scheme.HTTP, "localhost", null, 8080);
		restProxyFactory = new URLRestProxyFactory(hostDetails);
		uploadProxy = restProxyFactory.createProxy(UploadProxy.class);
	}
	
	@Test
	public void uploadFileTest() throws WebServiceException
	{
		final MultipartEntity multipartEntity = new MultipartEntity("test.txt", "this is the file contents.txt".getBytes());
		final byte[] response = uploadProxy.uploadFile(multipartEntity);
		Assert.assertEquals("test.txt", new String(response));
	}
}
