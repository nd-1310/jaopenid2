package com.apetrenko.jaopenid.consumer;

public class AuthStatus {

	public static final int OK 		= 1;
	public static final int SETUP 	= 2;
	public static final int CANCEL	= 4;
	public static final int ERROR	= 8;
	
	private final String iIdentity;
	private String iProvider;
	private final int iStatus;

	public AuthStatus(int aStatus) {
		iStatus = aStatus;
		iIdentity = "";
	}

	public AuthStatus(String aIdentity, String aProvider) {
		iIdentity = aIdentity;
		iProvider = aProvider;
		iStatus = OK;
	}

	public boolean isOk() {
		return iStatus == OK;
	}

	public String getIdentity() {
		return iIdentity;
	}
	
	public String getProvider(){
		return iProvider;
	}
	
	public int getStatus(){
		return iStatus;
	}

}
