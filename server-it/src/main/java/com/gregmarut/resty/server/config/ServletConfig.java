package com.gregmarut.resty.server.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan(basePackages =
{
	"com.gregmarut.resty.server"
}, excludeFilters =
{
	@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = AppConfig.class)
})
@EnableWebMvc
public class ServletConfig extends WebMvcConfigurerAdapter
{
}
