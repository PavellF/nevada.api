package org.pavelf.nevada.api.security;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.pavelf.nevada.api.exception.ExceptionCases;
import org.pavelf.nevada.api.exception.WebApplicationException;
import org.pavelf.nevada.api.logging.Logger;
import org.pavelf.nevada.api.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Aspect to perform checking annotated with {@link Secured} controllers.
 * @since 1.0
 * @author Pavel F.
 * */
@Aspect
@Component
public class TokenValidatorAspect {

	private  HttpServletRequest request;
	private TokenContext context;
	private Logger log;
	
	@Autowired
	public TokenValidatorAspect(HttpServletRequest request, 
			TokenContext context, 
			LoggerFactory factory) {
		this.request = request;
		this.context = context;
		this.log = factory.obtain(TokenValidatorAspect.class);
	}
	
	/**
	 * Decides whether request token has appropriate access to the controller 
	 * method annotated with {@link Secured}.
	 * */
	@Before("@annotation(secured)")
	public void process(Secured secured) throws Throwable {
		String ip = request.getRemoteAddr();
		boolean proceed = false;
		
		if (context.isAuthorized()) {
			Token token = context.getToken();
			proceed = token.hasAccess(secured.access(), secured.scope());
		}
		
		if (!proceed) {
			log.info(ip + " Token is disallowed to call this method.");
			throw new WebApplicationException(ExceptionCases.ACCESS_DENIED);
		} 
		
		log.info(ip + " Token has neccessary access level. Proceed..");
	}
	
	


}
