package com.gregmarut.resty.server.exception;

public class UserException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	public UserException(final String message)
	{
		super(message);
	}
}
