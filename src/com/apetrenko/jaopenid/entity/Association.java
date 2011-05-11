package com.apetrenko.jaopenid.entity;

import java.math.BigInteger;
import java.net.URL;

public class Association {

	private String iHandle;
	private BigInteger iMac;
	private int iTtl;
	private long iTimestamp;
	private String iLocalID;
	private URL iUrl;

	public Association(String aHandle, BigInteger aMac, int aTtl, String aLocalID, URL aUrl) {
		iTimestamp = System.currentTimeMillis();
		setHandle(aHandle);
		setMac(aMac);
		setLocalID(aLocalID);
		iTtl = aTtl;
		iUrl = aUrl;
	}

	protected void setLocalID(String aLocalID) {
		iLocalID = aLocalID;		
	}

	public boolean isValid() {
		return (System.currentTimeMillis() - iTimestamp) < (iTtl * 1000);
	}
	
	public String getLocalId(){
		return iLocalID;
	}
	
	public String getIdentity(){
		return iLocalID;
	}

	/**
	 * @param handle
	 *            the handle to set
	 */
	protected void setHandle(String aHandle) {
		iHandle = aHandle;
	}

	/**
	 * @return the handle
	 */
	public String getHandle() {
		return iHandle;
	}

	/**
	 * @param aMac
	 *            the mac to set
	 */
	protected void setMac(BigInteger aMac) {
		iMac = aMac;
	}

	/**
	 * @return the mac
	 */
	public BigInteger getMac() {
		return iMac;
	}
	
	public URL getURL(){
		return iUrl;
	}

}
