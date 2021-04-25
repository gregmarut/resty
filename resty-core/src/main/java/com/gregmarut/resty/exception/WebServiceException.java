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
package com.gregmarut.resty.exception;

public class WebServiceException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	private Object errorEntity;
	
	public WebServiceException(final String message)
	{
		super(message);
	}
	
	public WebServiceException(final String message, Object errorEntity)
	{
		super(message);
		setErrorEntity(errorEntity);
	}
	
	public WebServiceException(final Exception cause)
	{
		super(cause);
	}
	
	public WebServiceException(final Exception cause, Object errorEntity)
	{
		super(cause);
		setErrorEntity(errorEntity);
	}
	
	/**
	 * @return the error entity
	 */
	public <T> T getErrorEntity()
	{
		return (T) errorEntity;
	}
	
	/**
	 * @param errorEntity the error entity to set
	 */
	public void setErrorEntity(Object errorEntity)
	{
		this.errorEntity = errorEntity;
	}
}
