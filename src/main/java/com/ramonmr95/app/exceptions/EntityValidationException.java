package com.ramonmr95.app.exceptions;

public class EntityValidationException extends Exception {

	private static final long serialVersionUID = 1L;

	public EntityValidationException() {

	}

	public EntityValidationException(String message) {
		super(message);
	}

}
