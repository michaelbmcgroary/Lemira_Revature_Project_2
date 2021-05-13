package com.revature.exception;

@SuppressWarnings("serial")
public class NotModeratorException extends Exception {

	public NotModeratorException() {
		super();
	}

	public NotModeratorException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NotModeratorException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotModeratorException(String message) {
		super(message);
	}

	public NotModeratorException(Throwable cause) {
		super(cause);
	}

}
