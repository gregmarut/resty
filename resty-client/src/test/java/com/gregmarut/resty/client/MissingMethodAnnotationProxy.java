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

import com.gregmarut.resty.MethodType;
import com.gregmarut.resty.annotation.Body;
import com.gregmarut.resty.annotation.Parameter;
import com.gregmarut.resty.annotation.RestMethod;
import com.gregmarut.resty.annotation.RestProxy;
import com.gregmarut.resty.exception.WebServiceException;

@RestProxy(strict=true)
public interface MissingMethodAnnotationProxy
{
	@RestMethod(uri = "/test", method = MethodType.GET)
	String entityMethod(@Body("entityParam") String entityParam) throws WebServiceException;
	
	String id(@Parameter("id") int id) throws WebServiceException;
}
