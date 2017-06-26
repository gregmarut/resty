package com.gregmarut.resty.server.controller;

import com.gregmarut.resty.bean.UserBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/basic")
public class BasicAuthController
{
	@RequestMapping(value = "/me", method = RequestMethod.GET)
	public UserBean getUser()
	{
		UserBean userBean = new UserBean();
		userBean.setFirstName("Greg");
		userBean.setLastName("Marut");
		
		return userBean;
	}
}
