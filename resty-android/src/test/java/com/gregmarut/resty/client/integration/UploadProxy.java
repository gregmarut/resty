package com.gregmarut.resty.client.integration;

import com.gregmarut.resty.MethodType;
import com.gregmarut.resty.annotation.RestMethod;
import com.gregmarut.resty.annotation.RestProxy;
import com.gregmarut.resty.exception.WebServiceException;
import com.gregmarut.resty.http.request.MultipartEntity;

@RestProxy
public interface UploadProxy
{
	@RestMethod(uri = "/upload", method = MethodType.POST)
	byte[] uploadFile(final MultipartEntity multipartEntity) throws WebServiceException;
}
