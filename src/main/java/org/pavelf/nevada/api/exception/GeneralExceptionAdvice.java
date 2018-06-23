package org.pavelf.nevada.api.exception;

import java.util.Locale;

import org.pavelf.nevada.api.logging.Logger;
import org.pavelf.nevada.api.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * All exceptions ever thrown in time of processing request will be intercepted 
 * and routed and mapped to this class handlers.
 * @author Pavel F.
 * @since 1.0
 * */
@RestControllerAdvice
@Component
public class GeneralExceptionAdvice  {

	private MessageSource messageSource;
	private Logger log;
	
	@Autowired
	public GeneralExceptionAdvice(MessageSource messageSource, 
			LoggerFactory loggerFactory) {
		this.messageSource = messageSource;
		this.log = loggerFactory.obtain(getClass());
	}
	
	@ExceptionHandler(WebApplicationException.class)
	public ResponseEntity<ExceptionCase> webApplicationExceptionHandler(
			WebApplicationException execption, Locale locale) {
		ExceptionCase info = execption.getExceptionCase();
		
		log.error("THROWN: " + info.toString());
		
		if (locale == null) {
			locale = Locale.ENGLISH;
		}
		
		log.error("Incoming request locale was set: " + locale.toString());
			
		String message = messageSource.getMessage(
				String.valueOf(info.getCode()), null, locale);
			
		log.error("Appropriate message was just found: " + message);
			
		info = info.setMessage(message);
		
		log.error("Sending exception object to the caller...");
		
		return ResponseEntity.status(info.getHttpStatus()).body(info);
	}


}
