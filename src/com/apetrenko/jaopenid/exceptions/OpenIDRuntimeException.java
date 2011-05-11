package com.apetrenko.jaopenid.exceptions;

public class OpenIDRuntimeException extends RuntimeException {

	public OpenIDRuntimeException(String aMessage) {
		super(aMessage);
	}

	public OpenIDRuntimeException(String aMessage, Throwable aCause) {
		super(aMessage, aCause);
	}

	public OpenIDRuntimeException(Throwable aCause) {
		super(aCause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6317281537370278079L;

}
