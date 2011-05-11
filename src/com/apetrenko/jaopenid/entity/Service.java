package com.apetrenko.jaopenid.entity;

import java.net.MalformedURLException;
import java.net.URL;

import com.apetrenko.jaopenid.messages.Message;

public class Service implements Comparable<Service> {

	private int iVersion;
	private int iPriority;
	private URL iUrl;
	private String iLocalID;

	public void setPriority(int aPriority) {
		iPriority = aPriority;
	}

	@Override
	public int compareTo(Service aOther) {
		return aOther.iPriority - this.iPriority;
	}

	public void setURI(String aUri, int aPriority) {
		// TODO add support of priority.
		try {
			iUrl = new URL(aUri);
		} catch (MalformedURLException e) {
			// Just ignore.
		}
	}

	public void setType(String aType) {
		// TODO Replace with static constants.
		if ("http://specs.openid.net/auth/2.0/server".equals(aType)) {
			iVersion = Message.VERSION_2_0;
		} else if ("http://specs.openid.net/auth/2.0/signon".equals(aType)) {
			iVersion = Message.VERSION_2_0;
		} else if ("http://openid.net/signon/1.1".equals(aType)) {
			iVersion = Message.VERSION_1_1;
		} else if ("http://openid.net/signon/1.0".equals(aType)) {
			iVersion = Message.VERSION_1_0;
		}
	}

	public int getVersion() {
		return iVersion;
	}

	private static boolean isSecure(URL aUrl) {
		return aUrl.getProtocol().equalsIgnoreCase("https");
	}

	public String getIdentity() {
		if (iLocalID == null) {
			return "http://specs.openid.net/auth/2.0/identifier_select";
		} else {
			return iLocalID;
		}
	}

	public String getClaimedId() {
		return getIdentity();
	}

	public URL getUrl() {
		return iUrl;
	}

	public void setIdentity(String aLocalID) {
		iLocalID = aLocalID;
	}

	public boolean isSecure() {
		return isSecure(iUrl);
	}

}
