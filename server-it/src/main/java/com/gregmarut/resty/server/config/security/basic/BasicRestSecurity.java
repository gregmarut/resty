package com.gregmarut.resty.server.config.security.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

@Configuration
@EnableWebSecurity
public class BasicRestSecurity extends WebSecurityConfigurerAdapter
{
	
	@Autowired
	public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception
	{
		// set the user details service
		auth.userDetailsService(new TestUserDetailsService());
	}
	
	@Override
	protected void configure(final HttpSecurity http) throws Exception
	{
		// set the authentication to http basic
		RestAuthenticationEntryPoint entryPoint = new RestAuthenticationEntryPoint();
		entryPoint.setRealmName("Resty Basic");
		http.httpBasic().authenticationEntryPoint(entryPoint);
		http.exceptionHandling().authenticationEntryPoint(entryPoint);
		
		// disable cross site request forgery
		http.csrf().disable();
		
		// disable strict transport security
		http.headers().httpStrictTransportSecurity().disable();
		
		// configure the interceptors
		configureInterceptors(http.authorizeRequests());
	}
	
	private void configureInterceptors(
		final ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry intercept)
	{
		intercept.antMatchers("/auth/basic/**").authenticated();
		intercept.antMatchers("/**").permitAll();
	}
}
