package com.gregmarut.resty.client.integration;

import com.gregmarut.resty.bean.UserBean;
import com.gregmarut.resty.MethodType;
import com.gregmarut.resty.annotation.Expected;
import com.gregmarut.resty.annotation.RestMethod;
import com.gregmarut.resty.annotation.RestProxy;
import com.gregmarut.resty.exception.WebServiceException;
import org.apache.http.HttpStatus;

@RestProxy
public interface ObjectProxy
{
	@RestMethod(uri = "/object/user", method = MethodType.GET)
	UserBean getUser() throws WebServiceException;
	
	@RestMethod(uri = "/object/user", method = MethodType.PUT)
	@Expected(statusCode = HttpStatus.SC_CREATED)
	void putUser(UserBean userBean) throws WebServiceException;
	
	@RestMethod(uri = "/object/bytes", method = MethodType.GET)
	byte[] getBytes() throws WebServiceException;
}
