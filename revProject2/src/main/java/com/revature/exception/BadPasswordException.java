package com.revature.exception;


@SuppressWarnings("serial")
public class BadPasswordException extends Exception {

	public BadPasswordException() {
	}

	public BadPasswordException(String message) {
		super(message);
	}

	public BadPasswordException(Throwable cause) {
		super(cause);
	}

	public BadPasswordException(String message, Throwable cause) {
		super(message, cause);
	}

	public BadPasswordException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
