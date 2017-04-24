package com.random1312.calculator;


public class InvalidFormatException extends Exception {
	
	private static final long serialVersionUID = -607082931511652322L;

	public InvalidFormatException() {
		super();
	}
	
	public InvalidFormatException(String msg) {
		super(msg);
	}
}
