package com.ramonmr95.app.exceptions;

/**
 * 
 * Exception thrown when trying to find an entity that does not exist.
 * 
 * @author Ramón Moñino Rubio
 *
 */
public class EntityNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public EntityNotFoundException() {

	}

	public EntityNotFoundException(String message) {
		super(message);
	}

}
