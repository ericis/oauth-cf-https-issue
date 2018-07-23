package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static Logger log = LoggerFactory.getLogger(SecurityConfig.class);

	@Value("${my.https.enabled:false}")
	private boolean isHttpsEnabled;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.
			authorizeRequests().
				antMatchers("/anonymous*").anonymous().
				anyRequest().authenticated().and().
			oauth2Login();
		
		if (this.isHttpsEnabled) {
			log.info("HTTPS required");
			http.requiresChannel().anyRequest().requiresSecure().antMatchers("/login*", "/perform_login");
		} else {
			log.info("HTTPS not required");
		}
	}
}
