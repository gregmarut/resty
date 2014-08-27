/*******************************************************************************
 * Copyright (c) 2014 Greg Marut.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *    Greg Marut - initial API and implementation
 ******************************************************************************/
package com.gregmarut.resty.client;

import com.gregmarut.resty.client.annotation.Body;
import com.gregmarut.resty.client.annotation.HttpHeaders;
import com.gregmarut.resty.client.annotation.HttpParameters;
import com.gregmarut.resty.client.annotation.NameValue;
import com.gregmarut.resty.client.annotation.Parameter;
import com.gregmarut.resty.client.annotation.RestMethod;
import com.gregmarut.resty.client.annotation.RestProxy;
import com.gregmarut.resty.client.exception.WebServiceException;

@RestProxy
public interface TestInterfaceProxy
{
	@RestMethod(uri = "/test", method = MethodType.GET)
	String entityMethod(@Body("entityParam") String entityParam) throws WebServiceException;
	
	@RestMethod(uri = "/test", method = MethodType.GET)
	String transientMethod(@Parameter("transientParam") String transientParam) throws WebServiceException;
	
	@RestMethod(uri = "/test/{id}/", method = MethodType.GET)
	String id(@Parameter("id") int id) throws WebServiceException;
	
	@RestMethod(uri = "/test/{valueA}/{valueB}/", method = MethodType.GET)
	String multiValue(@Parameter("valueA") String valueA, @Parameter("valueB") String valueB)
		throws WebServiceException;
	
	@RestMethod(uri = "/test/{entityA}/{entityB}/", method = MethodType.GET)
	String invalidTwoEntities(@Body("entityA") String entityA, @Body("entityB") String entityB)
		throws WebServiceException;
	
	@RestMethod(uri = "/test/{entityA}/{entityB}/", method = MethodType.GET)
	String invalidTwoAnnotations(@Body("entityA") @Parameter("entityA") String entityA) throws WebServiceException;
	
	@RestMethod(uri = "/test", method = MethodType.GET)
	@HttpHeaders(
	{
		@NameValue(name = "Accepts", value = "application/json")
	})
	String headerMethodAccepts() throws WebServiceException;
	
	@RestMethod(uri = "/test", method = MethodType.GET)
	@HttpParameters(
	{
		@NameValue(name = "something", value = "some value")
	})
	String parameterMethodAccepts() throws WebServiceException;
}
