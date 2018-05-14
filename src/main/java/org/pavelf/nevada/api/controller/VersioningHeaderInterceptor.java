package org.pavelf.nevada.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pavelf.nevada.api.exception.ExceptionCase;
import org.pavelf.nevada.api.exception.ExceptionCases;
import org.pavelf.nevada.api.exception.WebApplicationException;
import org.pavelf.nevada.api.logging.Logger;
import org.pavelf.nevada.api.logging.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;
import static org.pavelf.nevada.api.exception.ExceptionCases.*;

/**
 * Checks whether request contain Accept header with version information.
 * @since 1.0
 * @author Pavel F.
 * */
public class VersioningHeaderInterceptor implements HandlerInterceptor {

	private final Logger logger;
	
	/**
	 * @param factory logger to use.
	 * */
	public VersioningHeaderInterceptor(LoggerFactory factory) {
		this.logger = factory.obtain(getClass());
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception {
		logger.debug("Starting processing incomig request's Accept header...");
		String header = request.getHeader("Accept");
		
		if (header == null) {
			throw new WebApplicationException(HEADER_SHOULD_BE_SPECIFIED);
		}
		logger.debug("Successfully resolved accept header value.");
		
		if (!isHeaderHasVersionAttribute(header)) {
			throw new WebApplicationException(MALFORMED_ACCEPT_HEADER);
		}
		
		return true;
	}
	
	public boolean isHeaderHasVersionAttribute(String header) {
		String[] splitted = header.split(";");
		
		for (String part : splitted) {
			if (part.startsWith("version=")) {
				logger.debug("Successfully resolved accept header value's version rapameter.");
				return true;
			}
		}
		return false;
	}
	
}
