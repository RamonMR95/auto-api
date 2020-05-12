package com.ramonmr95.app.exceptions;

/**
 * 
 * Exception thrown when the given id in string format cannot be parsed to
 * {@link UUID} format.
 * 
 * @author Ramón Moñino Rubio
 *
 */
public class InvalidUUIDFormatException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidUUIDFormatException() {

	}

	public InvalidUUIDFormatException(String message) {
		super(message);
	}
}
