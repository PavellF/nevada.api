package org.pavelf.nevada.api.exception;

import org.springframework.http.HttpStatus;

/**
 * Defines application specific exception information. Immutable. Usually serialized and send to client
 * over the wire in form of json or xml.
 * @since 1.0
 * @author Pavel F.
 * */
public class ExceptionCase {

	private final int code;
	private final String message;
	private final HttpStatus httpStatus;
	
	private ExceptionCase(int code, String message, HttpStatus httpStatus) {
		this.code = code;
		this.message = message;
		this.httpStatus = httpStatus;
	}

	/**
	 * @param message message to send out. Usually observed in stack trace.
	 * @param code specific code which has special meaning for the application.
	 * @param httpStatus HTTP code associated with this exception case.
	 * @throws IllegalArgumentException if null or empty string passed.
	 * */
	public static ExceptionCase of (int code, String message, HttpStatus httpStatus) {
		if (message == null || httpStatus == null || message.isEmpty()) {
			throw new IllegalArgumentException("Null or empty string passed.");
		}
		return new ExceptionCase(code, message, httpStatus);
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	/**
	 * Returns new object.
	 * @param message message to send out.
	 * */
	public ExceptionCase setMessage(String message) {
		return new ExceptionCase(code, message, httpStatus);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Code: ");
		builder.append(code);
		builder.append(", message: ");
		builder.append(message);
		return builder.toString();
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}


}
