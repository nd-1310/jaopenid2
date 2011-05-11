package com.apetrenko.jaopenid.messages;

import com.apetrenko.jaopenid.OpenIDConsts;

public class AssociationRequest extends DirectRequest {

	public static final int HMAC_SHA256 = 21;
	public static final int HMAC_SHA1 = 22;

	public static final int DH_SHA256 = 31;
	public static final int DH_SHA1 = 32;
	public static final int DH_NO_ENCRYPT = 33;

	public AssociationRequest() {
		setVersion(VERSION_2_0);
		setMode(OpenIDConsts.ASSOCIATE);
	}

	public void setSessionType(int aSessionType) {
		switch (aSessionType) {
		case DH_SHA256:
			setSessionType(OpenIDConsts.SESSION_TYPE_SHA256);
			break;
		case DH_SHA1:
			setSessionType(OpenIDConsts.SESSION_TYPE_SHA1);
			break;
		default:
			setSessionType(OpenIDConsts.SESSION_TYPE_NONE);
			break;
		}
	}

	public void setSessionType(String aSessionType) {
		setParameter(OpenIDConsts.SESSION_TYPE, aSessionType);
	}

	public void setAssocType(int aAssocType) {
		switch (aAssocType) {
		case HMAC_SHA1:
			setAssocType(OpenIDConsts.ASSOC_TYPE_SHA1);
			break;
		default:
			setAssocType(OpenIDConsts.ASSOC_TYPE_SHA256);
			break;
		}
	}

	public void setAssocType(String aAssocType) {
		setParameter(OpenIDConsts.ASSOC_TYPE, aAssocType);
	}

	public void setDhPublic(String aPublicKey) {
		setParameter(OpenIDConsts.DH_PUBLIC, aPublicKey);
	}

}
