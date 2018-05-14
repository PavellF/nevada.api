package org.pavelf.nevada.api.exception;

import javax.servlet.http.HttpServletRequest;

import org.pavelf.nevada.api.security.TokenContext;
import org.pavelf.nevada.api.security.Token;
import org.pavelf.nevada.api.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.annotation.RequestScope;

@RestControllerAdvice
@Component
public class GeneralExceptionAdvice  {

	private TokenContext tokenOwner;
	
	@Autowired
	public GeneralExceptionAdvice(TokenContext tokenOwner) {
		this.tokenOwner = tokenOwner;
	}
	
	
	@ExceptionHandler(WebApplicationException.class)
	public User fetchUser(WebApplicationException execption) {
		System.out.println("THROWN " + execption.getMessage());
		return null;
	}


}
