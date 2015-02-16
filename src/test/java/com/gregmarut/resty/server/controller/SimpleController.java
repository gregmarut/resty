package com.gregmarut.resty.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/simple")
public class SimpleController
{
	@RequestMapping(value = "/one", method = RequestMethod.GET)
	public @ResponseBody
	int getOne()
	{
		return 1;
	}
	
	@RequestMapping(value = "/two", method = RequestMethod.GET)
	public @ResponseBody
	int getTwo()
	{
		return 2;
	}
	
	@RequestMapping(value = "/value/{value}", method = RequestMethod.GET)
	public @ResponseBody
	int getValue(@PathVariable("value") final int value)
	{
		return value;
	}
}
