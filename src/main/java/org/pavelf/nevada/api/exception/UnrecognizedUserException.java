package org.pavelf.nevada.api.exception;

/**
 * Shortcut for {@code new WebApplicationException(UNRECOGNIZED_USER)}.
 * @author Pavel F.
 * @since 1.0
 * */
public class UnrecognizedUserException extends WebApplicationException {

	private static final long serialVersionUID = 3593035892703426602L;

	public UnrecognizedUserException() {
		super(ExceptionCases.UNRECOGNIZED_USER);
	}

}
