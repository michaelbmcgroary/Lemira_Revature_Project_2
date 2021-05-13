package com.revature.exception;

@SuppressWarnings("serial")
public class ReviewNotFoundException extends Exception {

	public ReviewNotFoundException() {
	}

	public ReviewNotFoundException(String message) {
		super(message);
	}

	public ReviewNotFoundException(Throwable cause) {
		super(cause);
	}

	public ReviewNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReviewNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
