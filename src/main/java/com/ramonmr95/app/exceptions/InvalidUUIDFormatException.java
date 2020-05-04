package com.ramonmr95.app.exceptions;

public class InvalidUUIDFormatException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidUUIDFormatException() {

	}

	public InvalidUUIDFormatException(String message) {
		super(message);
	}
}
