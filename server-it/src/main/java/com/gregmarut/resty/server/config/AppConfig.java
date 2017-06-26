package com.gregmarut.resty.server.config;

import com.gregmarut.resty.server.config.security.basic.BasicRestSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(
{
	BasicRestSecurity.class
})
public class AppConfig
{
	
}
