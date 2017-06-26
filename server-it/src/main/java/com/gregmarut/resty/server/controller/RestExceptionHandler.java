package com.gregmarut.resty.server.controller;

import com.gregmarut.resty.bean.ErrorBean;
import com.gregmarut.resty.server.exception.ServerException;
import com.gregmarut.resty.server.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestExceptionHandler
{
	@ExceptionHandler(UserException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorBean handleException(final UserException e)
	{
		ErrorBean errorBean = new ErrorBean();
		errorBean.setStatus(HttpStatus.BAD_REQUEST.value());
		errorBean.setMessage(e.getMessage());
		
		return errorBean;
	}
	
	@ExceptionHandler(ServerException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ErrorBean handleException(final ServerException e)
	{
		ErrorBean errorBean = new ErrorBean();
		errorBean.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		errorBean.setMessage(e.getMessage());
		
		return errorBean;
	}
}
