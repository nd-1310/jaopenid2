package com.apetrenko.jaopenid.exceptions;


public class IdetifierException extends OpenIDException {

	public IdetifierException(String aMessage) {
		super(aMessage);
	}

	public IdetifierException(String aMessage, Throwable aCause) {
		super(aMessage, aCause);
	}

	private static final long serialVersionUID = -2058006408525717177L;

}
