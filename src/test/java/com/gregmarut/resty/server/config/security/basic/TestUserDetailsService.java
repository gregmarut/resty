package com.gregmarut.resty.server.config.security.basic;

import java.util.ArrayList;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class TestUserDetailsService implements UserDetailsService
{
	public static final String TEST_USERNAME = "admin";
	public static final String TEST_PASSWORD = "password";
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		if (TEST_USERNAME.equals(username))
		{
			return new User(TEST_USERNAME, TEST_PASSWORD, new ArrayList<GrantedAuthority>());
		}
		else
		{
			throw new UsernameNotFoundException(username + " is invalid");
		}
	}
}
