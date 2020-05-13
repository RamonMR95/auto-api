package com.ramonmr95.app.exceptions;

/**
 * 
 * Exception thrown when the unique contraint is violated.
 * 
 * @author Ramón Moñino Rubio
 *
 */
public class NotUniqueKeyException extends Exception {

	private static final long serialVersionUID = 1L;

	public NotUniqueKeyException() {

	}

	public NotUniqueKeyException(String message) {
		super(message);
	}
}
