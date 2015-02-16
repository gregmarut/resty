package com.gregmarut.resty.server.exception;

public class ServerException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	public ServerException(final String message)
	{
		super(message);
	}
}
