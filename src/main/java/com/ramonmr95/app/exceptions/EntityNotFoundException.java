package com.ramonmr95.app.exceptions;

public class EntityNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public EntityNotFoundException() {

	}

	public EntityNotFoundException(String message) {
		super(message);
	}

}
