package com.revature.exception;


@SuppressWarnings("serial")
public class UserAddException extends Exception {

	public UserAddException() {
	}

	public UserAddException(String message) {
		super(message);
	}

	public UserAddException(Throwable cause) {
		super(cause);
	}

	public UserAddException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserAddException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
