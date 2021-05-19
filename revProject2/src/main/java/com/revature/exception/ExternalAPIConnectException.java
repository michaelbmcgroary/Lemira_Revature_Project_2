package com.revature.exception;

@SuppressWarnings("serial")
public class ExternalAPIConnectException extends Exception {

	public ExternalAPIConnectException() {
	}

	public ExternalAPIConnectException(String message) {
		super(message);
	}

	public ExternalAPIConnectException(Throwable cause) {
		super(cause);
	}

	public ExternalAPIConnectException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExternalAPIConnectException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
