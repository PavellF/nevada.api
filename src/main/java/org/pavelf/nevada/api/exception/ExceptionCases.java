package org.pavelf.nevada.api.exception;

import org.springframework.http.HttpStatus;

/**
 * Describes domain-specific exception cases.
 * @author Pavel F.
 * @since 1.0
 * */
public interface ExceptionCases {

	public static final ExceptionCase MULTIPLE_IP_REQUESTS =
			ExceptionCase.of(1, "Too many requests that have the same token "
					+ "from different IP addresses has come in short "
					+ "period of time...", HttpStatus.FORBIDDEN);
	
	public final ExceptionCase HEADER_SHOULD_BE_SPECIFIED = 
			ExceptionCase.of(2, "Some of required headers are missing!", 
					HttpStatus.BAD_REQUEST);
	
	public static final ExceptionCase UNKNOWN_VERSION =
			ExceptionCase.of(3, 
					"Could not recognize incoming request's header version.", 
					HttpStatus.BAD_REQUEST);
	
	public static final ExceptionCase MALFORMED_VERSION =
			ExceptionCase.of(4, "Can not parse requested object version.", 
					HttpStatus.BAD_REQUEST);
	
	public static final ExceptionCase REQUIRED_BODY_PROPERTY =
			ExceptionCase.of(5, "One of required properties in the "
					+ "body was not set.", HttpStatus.BAD_REQUEST);
	
	public static final ExceptionCase UNRECOGNIZED_USER =
			ExceptionCase.of(6, "Could not recognize user.", 
					HttpStatus.FORBIDDEN);
	
	public static final ExceptionCase UNRECOGNIZED_APPLICATION =
			ExceptionCase.of(7, "Could not recognize application.", 
					HttpStatus.FORBIDDEN);
	
	public static final ExceptionCase BANNED_APPLICATION =
			ExceptionCase.of(8, "Specified application can not perform "
					+ "requests due suspend.", HttpStatus.FORBIDDEN);
	
	public static final ExceptionCase INVALID_BODY_PROPERTY =
			ExceptionCase.of(9, "Some of body property is invalid "
					+ "(e.g. invalid time).", HttpStatus.BAD_REQUEST);
	
	public static final ExceptionCase ACCESS_DENIED =
			ExceptionCase.of(10, "Have no permission to access this endpoint.", 
					HttpStatus.FORBIDDEN);
	
	public static final ExceptionCase OUT_OF_BOUND_VALUE =
			ExceptionCase.of(11, "Some value exeeds its limit.", 
					HttpStatus.BAD_REQUEST);
	
	public static final ExceptionCase BODY_REQUIRED =
			ExceptionCase.of(12, "Request body is required to "
					+ "perform this action.", HttpStatus.BAD_REQUEST);
	
	public static final ExceptionCase FAILED_UPDATE =
			ExceptionCase.of(13, "Could not update entity.", 
					HttpStatus.INTERNAL_SERVER_ERROR);
	
	public static final ExceptionCase NO_PREVIOUS_PASSWORD =
			ExceptionCase.of(14, "Need current password in order "
					+ "to update existing one.", HttpStatus.FORBIDDEN);
	
	public static final ExceptionCase BANNED_PROFILE =
			ExceptionCase.of(15, "This profile can not perform requests.", 
					HttpStatus.FORBIDDEN);
	
	public static final ExceptionCase FORBIDDEN_BODY_PROPERTY =
			ExceptionCase.of(16, "Some of body properties set are "
					+ "disallowed for this user.", HttpStatus.BAD_REQUEST);
	
	public static final ExceptionCase INVALID_REQUEST_PARAM =
			ExceptionCase.of(17, 
					"Some of request params are invalid, maybe some "
					+ "values out of bound.", HttpStatus.BAD_REQUEST);
	
	public static final ExceptionCase INTERNAL_SERVER_ERROR =
			ExceptionCase.of(18, "Something went wrong on server side.", 
					HttpStatus.INTERNAL_SERVER_ERROR);
	
	public static final ExceptionCase CAN_NOT_LIKE = ExceptionCase.of(
			19, 
			"Can not like object twice or like own content.", 
			HttpStatus.BAD_REQUEST);
	
	public static final ExceptionCase WRONG_TYPE = ExceptionCase.of(
			20, 
			"Different type expected. Consider sending integer value instead "
			+ "of string or vice versa.", 
			HttpStatus.BAD_REQUEST);
	
	public static final ExceptionCase PARSING_EXCEPTION = ExceptionCase.of(
			21, 
			"Could not parse input value. For instance, consider passing "
			+ "string that consist of numeric value when trying to parse "
			+ "integer.", HttpStatus.BAD_REQUEST);
	
	public static final ExceptionCase UPLOADED_FILE_IS_TOO_LARGE = 
			ExceptionCase.of(
			22, 
			"Uploaded file is too large.",
			HttpStatus.BAD_REQUEST);
}
