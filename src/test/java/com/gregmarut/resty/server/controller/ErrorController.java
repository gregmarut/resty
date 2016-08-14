package com.gregmarut.resty.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gregmarut.resty.server.exception.ServerException;

@Controller
@RequestMapping("/error")
public class ErrorController
{
	@RequestMapping(value = "/500")
	public void error500()
	{
		throw new ServerException("500 Error");
	}
	
	@RequestMapping(value = "/500/2")
	public void error500_2()
	{
		throw new ServerException("500 Error");
	}
}
