package com.apetrenko.jaopenid.messages;

import java.math.BigInteger;
import java.net.URL;

import com.apetrenko.jaopenid.entity.Association;

public class AssociationResponse extends DirectResponse {
	
	public static final String ASSOC_HANDLE = "assoc_handle";
	public static final String MAC_KEY = "mac_key";
	public static final String EXPIRES = "expires_in";

	public Association getAssociation(BigInteger aMac, String aLocalID, URL aUrl) {
		Association vResult = null;// new Association(iHandle, aMac, iTtl,
									// aLocalID, aUrl);
		return vResult;
	}
	
	public String getHandle(){
		return getParameter(ASSOC_HANDLE);
	}
	
	public String getMac(){
		return getParameter(MAC_KEY);
	}

	@Override
	public boolean isOk() {
		return true;
	}
	
	public int getTtl() {
		String vTtl = getParameter(EXPIRES);
		if (vTtl != null){
			return Integer.parseInt(vTtl);
		} else {
			return 0;
		}
	}
}
