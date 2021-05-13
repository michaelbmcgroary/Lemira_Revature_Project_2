package com.revature.exception;

@SuppressWarnings("serial")
public class ReviewAddException extends Exception {

	public ReviewAddException() {
	}

	public ReviewAddException(String message) {
		super(message);
	}

	public ReviewAddException(Throwable cause) {
		super(cause);
	}

	public ReviewAddException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReviewAddException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
