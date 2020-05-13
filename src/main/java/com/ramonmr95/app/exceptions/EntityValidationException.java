package com.ramonmr95.app.exceptions;

/**
 * 
 * Exception thrown when the entity does not fulfill all of the bean
 * validations.
 * 
 * @author Ramón Moñino Rubio
 *
 */
public class EntityValidationException extends Exception {

	private static final long serialVersionUID = 1L;

	public EntityValidationException() {

	}

	public EntityValidationException(String message) {
		super(message);
	}

}
