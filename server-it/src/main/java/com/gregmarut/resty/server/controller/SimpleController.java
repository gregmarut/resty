package com.gregmarut.resty.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simple")
public class SimpleController
{
	@GetMapping("/one")
	public int getOne()
	{
		return 1;
	}
	
	@GetMapping("/two")
	public int getTwo()
	{
		return 2;
	}
	
	@GetMapping("/value/{value}")
	public int getValue(@PathVariable("value") final int value)
	{
		return value;
	}
}
