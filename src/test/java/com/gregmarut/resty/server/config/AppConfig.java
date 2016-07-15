package com.gregmarut.resty.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gregmarut.resty.server.config.security.basic.BasicRestSecurity;

@Configuration
@Import(
{
	BasicRestSecurity.class
})
public class AppConfig
{
	
}
