package com.random1312.calculator;

public class CalculationException extends Exception{

	private static final long serialVersionUID = -3066935521651898013L;

	public CalculationException(String s) {
		super(s);
	}
	
	public CalculationException() {
		super();
	}
}
