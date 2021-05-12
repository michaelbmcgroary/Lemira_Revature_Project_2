package com.revature.exception;


@SuppressWarnings("serial")
public class PasswordHashException extends Exception {

	public PasswordHashException() {
	}

	public PasswordHashException(String message) {
		super(message);
	}

	public PasswordHashException(Throwable cause) {
		super(cause);
	}

	public PasswordHashException(String message, Throwable cause) {
		super(message, cause);
	}

	public PasswordHashException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
