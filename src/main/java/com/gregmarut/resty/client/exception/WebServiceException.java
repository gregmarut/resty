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
package com.gregmarut.resty.client.exception;

public class WebServiceException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	private Object errorEntity;
	
	public WebServiceException(final String message)
	{
		super(message);
	}
	
	public WebServiceException(final Exception cause)
	{
		super(cause);
	}
	
	/**
	 * @return the error entity
	 */
	public Object getErrorEntity()
	{
		return errorEntity;
	}
	
	/**
	 * @param error
	 * the error entity to set
	 */
	public void setErrorEntity(Object errorEntity)
	{
		this.errorEntity = errorEntity;
	}
}
