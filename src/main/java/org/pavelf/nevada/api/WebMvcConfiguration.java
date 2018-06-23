package org.pavelf.nevada.api;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.pavelf.nevada.api.exception.ExceptionCases;
import org.pavelf.nevada.api.exception.WebApplicationException;

import org.pavelf.nevada.api.logging.LoggerFactory;
import org.pavelf.nevada.api.resolver.PageAndSortResolver;
import org.pavelf.nevada.api.resolver.VersionConverter;
import org.pavelf.nevada.api.security.IpTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
	    		new IpTokenInterceptor(Duration.of(5, ChronoUnit.SECONDS),
	    				logger, (String token) -> {
	    					throw new WebApplicationException(
	    							ExceptionCases.MULTIPLE_IP_REQUESTS);
	    			})).order(1);
	 }
	
	@Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new VersionConverter());
    }
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = 
				new ReloadableResourceBundleMessageSource();
		messageSource.setConcurrentRefresh(false);
		messageSource.setBasenames("classpath:locale/ExceptionCases");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
	
	@Override
    public void addArgumentResolvers(
    		List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new PageAndSortResolver());
    }
	
}
