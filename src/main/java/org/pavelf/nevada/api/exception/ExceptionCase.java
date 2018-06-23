package org.pavelf.nevada.api.exception;

import org.springframework.http.HttpStatus;

/**
 * Encapsulates application specific exception information.
 * May be serialized and send to client over the wire in form of json or xml.
 * @since 1.0
 * @author Pavel F.
 * */
public class ExceptionCase {

	private int code;
	private String message;
	private HttpStatus httpStatus;
	
	private ExceptionCase(int code, String message, HttpStatus httpStatus) {
		this.code = code;
		this.message = message;
		this.httpStatus = httpStatus;
	}
	
	private ExceptionCase() { }
	
	/**
	 * @param message message to send out. Usually observed in stack trace.
	 * @param code specific code which has special meaning for the application.
	 * @param httpStatus HTTP code associated with this exception case.
	 * @throws IllegalArgumentException if null or empty string passed.
	 * */
	public static ExceptionCase of(int code, String message, 
			HttpStatus httpStatus) {
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

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		result = prime * result
				+ ((httpStatus == null) ? 0 : httpStatus.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExceptionCase other = (ExceptionCase) obj;
		if (code != other.code)
			return false;
		if (httpStatus != other.httpStatus)
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ExceptionCase [code=");
		builder.append(code);
		builder.append(", message=");
		builder.append(message);
		builder.append(", httpStatus=");
		builder.append(httpStatus);
		builder.append("]");
		return builder.toString();
	}

}
