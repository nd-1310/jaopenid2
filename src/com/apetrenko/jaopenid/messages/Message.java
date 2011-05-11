package com.apetrenko.jaopenid.messages;

import java.util.Hashtable;
import java.util.Map;

import com.apetrenko.jaopenid.OpenIDConsts;
import com.apetrenko.jaopenid.exceptions.OpenIDRuntimeException;

public abstract class Message {

	public static final int VERSION_1_0 = 10;
	public static final int VERSION_1_1 = 11;
	public static final int VERSION_2_0 = 20;

	private Hashtable<String, String> iParams;

	public Message() {
		iParams = new Hashtable<String, String>();
	}

	public final String getParameter(String aKey) {
		return iParams.get(aKey);
	}
	
	public final void setParameter(String aKey, String aValue) {
		if (aKey == null){
			throw new OpenIDRuntimeException("Parameter key cannot be null");
		} else if (aValue == null){
			throw new OpenIDRuntimeException("Parameter value cannot be null");
		}
		iParams.put(aKey, aValue);
	}

	public final void setVersion(int aVersion) {
		switch (aVersion) {
		case VERSION_2_0:
			setVersion(OpenIDConsts.MESSAGE_V_20);
			break;
		case VERSION_1_1:
			setVersion(OpenIDConsts.MESSAGE_V_11);
			break;
		default:
			setVersion(OpenIDConsts.MESSAGE_V_10);
			break;
		}
	}

	public final void setVersion(String aVersion) {
		setParameter(OpenIDConsts.NS, aVersion);
	}

	public final String getVersion() { 		
		return getParameter(OpenIDConsts.NS);
	}
	
	public final void setMode(String aMode){
		setParameter(OpenIDConsts.MODE, aMode);
	}
	
	public final String getMode(){
		return getParameter(OpenIDConsts.MODE);
	}
	
	protected Map<String, String> getParameterMap(){
		return iParams;
	}
	
	public void setParameters(Map<String, String> aParams) {
		iParams.clear();
		iParams.putAll(aParams);
	}
	
	
	// TODO Implement required and optional fields check.
	//public abstract List<String> getRequiredFields();

	//public abstract List<String> getOptionalFields();

	
}
