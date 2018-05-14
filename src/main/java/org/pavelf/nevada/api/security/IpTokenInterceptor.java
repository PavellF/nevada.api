package org.pavelf.nevada.api.security;

import java.lang.annotation.Annotation;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.annotation.Aspect;
import org.pavelf.nevada.api.exception.WebApplicationException;
import org.pavelf.nevada.api.logging.Logger;
import org.pavelf.nevada.api.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * If some request invokes service with some token from different IP and minimal allowed duration 
 * between requests with same token and different IP has not yet been elapsed (e. g. token owner
 * move to different place), token is discarded. If no token in Access header then just go further in 
 * intercepter chain.  
 * @since 1.0
 * @author Pavel F.
 * */
public class IpTokenInterceptor implements HandlerInterceptor {

	private final Map<String, LatestIp> tokenIpMap; 
	private final Duration minAllowed;
	private final Logger logger;
	private final Consumer<String> callback;
	
	/**
	 * @param minAllowed time describing minimal amount of time must elapse to be able invoke request 
	 * with different IP.
	 * @param logger logger to use.
	 * @param callback describes what should be done when IPs change quickly. Typical use case is 
	 * delete associated token from database.
	 * */
	public IpTokenInterceptor(Duration minAllowed, LoggerFactory loggerFactory, 
			Consumer<String> callback) {
		 tokenIpMap = new ConcurrentHashMap<>();
		 this.minAllowed = minAllowed;
		 this.logger = loggerFactory.obtain(getClass());
		 this.callback = callback;
	}
	
	/**
	 * @return true or false and supplied callback gets called.
	 * @throws WebApplicationException if conditions are not met.
	 * */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception {
		String accessHeader = request.getHeader("Authorization");
		
		if (accessHeader != null) {
			logger.info("Starting processing incoming request with Authorization header: "+accessHeader);
			LatestIp latestIp = tokenIpMap.get(accessHeader);
			final String IpAddress = request.getRemoteAddr();
			
			if (latestIp != null && !latestIp.getIp().equals(IpAddress) &&
					Instant.now().minus(minAllowed).isBefore(latestIp.getLastRequest())) {
				logger.info("IPs are changing quickly which is disallowed.");
				tokenIpMap.remove(accessHeader);
				callback.accept(accessHeader);
				return false;
			}
			
			logger.info("Conditions met. Go next..");
			tokenIpMap.put(accessHeader, new LatestIp(IpAddress, Instant.now()));
		} else {
			logger.info("No Authorization header found, go next..");
		}
		return true;
	}
	
	
}
