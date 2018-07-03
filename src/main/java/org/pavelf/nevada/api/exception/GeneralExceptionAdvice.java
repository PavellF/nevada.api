package org.pavelf.nevada.api.exception;

import java.util.Locale;

import javax.validation.ConstraintViolationException;
import static org.pavelf.nevada.api.exception.ExceptionCases.*;

import org.pavelf.nevada.api.logging.Logger;
import org.pavelf.nevada.api.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * All exceptions ever thrown in time of processing request will be intercepted 
 * and routed to this class handlers.
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
		return handleExceptionCase(execption, locale, 
				execption.getExceptionCase());
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ExceptionCase> wrongTypeHandler(
			MethodArgumentTypeMismatchException execption, Locale locale) {
		
		return handleExceptionCase(execption, locale, WRONG_TYPE);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ExceptionCase> constraintViolation(
			ConstraintViolationException execption, Locale locale) {
		
		return handleExceptionCase(execption, locale, INVALID_BODY_PROPERTY);
	}
	
	@ExceptionHandler(NumberFormatException.class)
	public ResponseEntity<ExceptionCase> constraintViolation(
			NumberFormatException execption, Locale locale) {
		
		return handleExceptionCase(execption, locale, PARSING_EXCEPTION);
	}
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ExceptionCase> sizeLimitExceeded(Locale locale, 
			NullPointerException exception) {
		
		return handleExceptionCase(exception, locale, INTERNAL_SERVER_ERROR);
	}
	
	protected ResponseEntity<ExceptionCase> handleExceptionCase(
			Exception execption, Locale locale, ExceptionCase info) {

		log.error("THROWN: " + execption.toString());
		
		if (locale == null) {
			locale = Locale.ENGLISH;
		}
		
		log.error("Incoming request locale was set: " + locale.toString());
			
		String message = messageSource.getMessage(
				String.valueOf(info.getCode()), null, locale);
			
		log.error("Appropriate message was just found: " + message);
			
		info = info.setMessage(message);
		
		log.error("Sending exception object back to the caller...");
		
		return ResponseEntity.status(info.getHttpStatus()).body(info);
	}

}
