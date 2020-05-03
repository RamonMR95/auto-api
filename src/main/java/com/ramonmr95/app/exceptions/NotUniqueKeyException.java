package com.ramonmr95.app.exceptions;

public class NotUniqueKeyException extends Exception {

	private static final long serialVersionUID = 1L;

	public NotUniqueKeyException() {

	}

	public NotUniqueKeyException(String message) {
		super(message);
	}
}
