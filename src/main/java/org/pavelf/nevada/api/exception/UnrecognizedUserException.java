package org.pavelf.nevada.api.exception;

public class UnrecognizedUserException extends WebApplicationException {

	private static final long serialVersionUID = 3593035892703426602L;

	public UnrecognizedUserException() {
		super(ExceptionCases.UNRECOGNIZED_USER);
	}

}
