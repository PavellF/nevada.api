package org.pavelf.nevada.api;

import org.pavelf.nevada.api.security.TokenValidatorAspect;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import org.pavelf.nevada.api.exception.ExceptionCases;
import org.pavelf.nevada.api.exception.WebApplicationException;
import org.pavelf.nevada.api.interceptor.VersioningHeaderInterceptor;
import org.pavelf.nevada.api.logging.Logger;
import org.pavelf.nevada.api.logging.LoggerFactory;
import org.pavelf.nevada.api.security.IpTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

	private LoggerFactory logger;
	
	@Autowired
	public WebMvcConfiguration(LoggerFactory logger) {
		this.logger = logger;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(
	    		new IpTokenInterceptor(Duration.of(5, ChronoUnit.SECONDS), logger, 
	    				(String token) -> {
	    					throw new WebApplicationException(ExceptionCases.MULTIPLE_IP_REQUESTS);
	    				})).order(1);
	    registry.addInterceptor(new VersioningHeaderInterceptor(logger)).order(0);
	}
	
	
	
}
