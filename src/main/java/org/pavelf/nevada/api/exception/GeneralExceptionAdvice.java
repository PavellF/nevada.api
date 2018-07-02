package org.pavelf.nevada.api.exception;

import java.util.Locale;

import javax.validation.ConstraintViolationException;
import static org.pavelf.nevada.api.exception.ExceptionCases.*;

import org.apache.tomcat.util.http.fileupload.FileUploadBase;
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
	
	@ExceptionHandler(FileUploadBase.SizeLimitExceededException.class)
	public ResponseEntity<ExceptionCase> sizeLimitExceeded(Locale locale, 
			FileUploadBase.SizeLimitExceededException e) {
		
		return handleExceptionCase(e, locale, UPLOADED_FILE_IS_TOO_LARGE);
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
