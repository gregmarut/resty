package com.gregmarut.resty.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gregmarut.resty.bean.UserBean;
import com.gregmarut.resty.server.exception.UserException;

@Controller
@RequestMapping("/object")
public class ObjectController
{
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public @ResponseBody
	UserBean getUser()
	{
		UserBean userBean = new UserBean();
		userBean.setFirstName("Greg");
		userBean.setLastName("Marut");
		
		return userBean;
	}
	
	@RequestMapping(value = "/user", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void putUser(@RequestBody UserBean userBean)
	{
		if (null != userBean && !"Greg".equals(userBean.getFirstName()) && !"Marut".equals(userBean.getLastName()))
		{
			throw new UserException("Wrong User");
		}
	}
}
