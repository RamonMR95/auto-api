package com.ramonmr95.app.utils;

public class CarNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public CarNotFoundException() {

	}

	public CarNotFoundException(String message) {
		super(message);
	}

}
