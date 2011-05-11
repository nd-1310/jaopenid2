package com.apetrenko.jaopenid.messages;

import java.net.URL;

import com.apetrenko.jaopenid.OpenIDConsts;

public class AuthenticationRequest extends IndirectRequest {

	public static final int IMMEDIATE = 41;
	public static final int SETUP = 42;

	public void setMode(int aMode) {
		switch (aMode) {
		case IMMEDIATE:
			setMode(OpenIDConsts.MODE_IMMEDIATE);
			break;
		case SETUP:
			setMode(OpenIDConsts.MODE_SETUP);
			break;
		}
	}

	public void setHandle(String aHandle) {
		setParameter(OpenIDConsts.ASSOC_HANDLE, aHandle);
	}

	public void setClaimedId(String aClaimedId) {
		setParameter(OpenIDConsts.CLAIMED_ID, aClaimedId);
	}

	public void setIdentity(String aIdentity) {
		setParameter(OpenIDConsts.IDENTITY, aIdentity);
	}	

	public void setReturnTo(URL aReturnTo) {
		setParameter(OpenIDConsts.RETURN_TO, aReturnTo.toExternalForm());
	}

	public void setRealm(String aRealm) {
		setParameter(OpenIDConsts.REALM, aRealm);
	}

}
