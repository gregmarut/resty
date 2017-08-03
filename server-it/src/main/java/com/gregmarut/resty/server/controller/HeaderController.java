package com.gregmarut.resty.server.controller;

import com.gregmarut.resty.server.exception.BadRequestException;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/header")
public class HeaderController
{
	@RequestMapping(value = "/verify", method = RequestMethod.GET)
	public void verifyHeader(@RequestHeader(value = "token", required = false) final String token, final HttpServletResponse response)
	{
		if (null == token)
		{
			throw new BadRequestException("Missing Header");
		}
		else if (!"TOKEN-ABCD1234".equals(token))
		{
			throw new BadRequestException("Invalid Header");
		}
	}
}
