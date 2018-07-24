package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static Logger log = LoggerFactory.getLogger(SecurityConfig.class);

	@Value("${my.https.enabled:true}")
	private boolean isHttpsEnabled;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.
			authorizeRequests().
				antMatchers("/anonymous*", "favicon.ico").anonymous().
				anyRequest().authenticated().and().
			oauth2Login();
		
		if (this.isHttpsEnabled) {
			log.info("HTTPS required");
			http.requiresChannel().anyRequest().requiresSecure().antMatchers("/login");
		} else {
			log.info("HTTPS not required");
		}
	}
	
	@Bean
	FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
		
	    final FilterRegistrationBean<ForwardedHeaderFilter> filterRegistrationBean = new FilterRegistrationBean<ForwardedHeaderFilter>();
	    
	    filterRegistrationBean.setFilter(new ForwardedHeaderFilter());
	    filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
	    
	    return filterRegistrationBean;
	}
}
