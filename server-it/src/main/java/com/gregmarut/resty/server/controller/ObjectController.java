package com.gregmarut.resty.server.controller;

import com.gregmarut.resty.bean.UserBean;
import com.gregmarut.resty.server.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/object")
public class ObjectController
{
	@GetMapping("/user")
	public UserBean getUser()
	{
		UserBean userBean = new UserBean();
		userBean.setFirstName("Greg");
		userBean.setLastName("Marut");
		
		return userBean;
	}
	
	@PutMapping("/user")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void putUser(@RequestBody UserBean userBean)
	{
		if (null != userBean && !"Greg".equals(userBean.getFirstName()) && !"Marut".equals(userBean.getLastName()))
		{
			throw new UserException("Wrong User");
		}
	}
	
	@GetMapping("/bytes")
	public byte[] getBytes()
	{
		return "Hello".getBytes();
	}
}
