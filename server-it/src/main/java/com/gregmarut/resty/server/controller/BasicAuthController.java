package com.gregmarut.resty.server.controller;

import com.gregmarut.resty.bean.UserBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/basic")
public class BasicAuthController
{
	@GetMapping("/me")
	public UserBean getUser()
	{
		UserBean userBean = new UserBean();
		userBean.setFirstName("Greg");
		userBean.setLastName("Marut");
		
		return userBean;
	}
}
