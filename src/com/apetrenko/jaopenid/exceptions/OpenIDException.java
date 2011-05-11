package com.apetrenko.jaopenid.exceptions;

public class OpenIDException extends Exception {

	public OpenIDException(String aMessage) {
		super(aMessage);
	}

	public OpenIDException(String aMessage, Throwable aCause) {
		super(aMessage, aCause);
	}

	public OpenIDException() {
		// TODO Auto-generated constructor stub
	}

	public OpenIDException(Throwable aCause) {
		super(aCause);
	}

	private static final long serialVersionUID = -4423695646796889387L;

}
