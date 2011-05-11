package com.apetrenko.jaopenid.exceptions;

public class DiscoveryException extends OpenIDException {

	private static final long serialVersionUID = -1878118528472545077L;

	public DiscoveryException(String aMessage) {
		super(aMessage);
	}

	public DiscoveryException(String aMessage, Exception aCause) {
		super(aMessage, aCause);
	}

	public DiscoveryException() {
		super();
	}

}
