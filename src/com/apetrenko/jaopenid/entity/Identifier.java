package com.apetrenko.jaopenid.entity;

import java.net.MalformedURLException;
import java.net.URL;

import com.apetrenko.jaopenid.exceptions.IdetifierException;

public class Identifier {

	private static final String XRI_PREFIX = "xri://";
	private static final String XRI_GLOBAL_CONTEXT_REGEX = "[\\=\\@\\+\\%\\!\\)].*";
	private static final String HTTP_FRAGMENT_REGEX = "\\#.*";
	private static final String HTTP_HOSTNAME_REGEX = "(http|https):\\/\\/(.+[.])+\\w{2,}";
	private static final String URL_PROTO_REGEX = "\\w*:\\/\\/.*";

	private String iId;
	private URL iUrl;
	private boolean iIsXri;

	private Identifier(String aId, boolean aIsXri) throws MalformedURLException {
		iId = aId;
		iIsXri = aIsXri;
		if (!isXRI()){
			iUrl = new URL(iId);
		}
	}

	private static String normalize(String aId) throws IdetifierException {
		String vResult;
		if (isXRI(aId)) {
			vResult = normalizeXRI(aId);
		} else {
			vResult = normalizeURL(aId);
		}
		return vResult;
	}

	private static String normalizeURL(String aId) throws IdetifierException {
		String vResult = aId.replaceAll(HTTP_FRAGMENT_REGEX, "");
		boolean vIsValid = false;
		if (!vResult.toLowerCase().matches(URL_PROTO_REGEX)) {
			vResult = "http://" + vResult;
		}
		if (vResult.matches(HTTP_HOSTNAME_REGEX + ".*")) {
			if (!vResult.matches(HTTP_HOSTNAME_REGEX + "\\/.*")){
				vResult = vResult + "/";
			}			
			vIsValid = true;
		}
		if (!vIsValid) {
			throw new IdetifierException("Supplied ID is neither XRI nor URL.");
		}
		return vResult;
	}

	private static String normalizeXRI(String aId) {
		if (aId.toLowerCase().startsWith(XRI_PREFIX)) {
			return aId.replaceFirst(XRI_PREFIX, "");
		}
		return aId;
	}

	private static boolean isXRI(String aId) {
		String vId = aId.toLowerCase();
		boolean vIsXri = false;
		if (vId.startsWith(XRI_PREFIX)) {
			vId = vId.replaceFirst(XRI_PREFIX, "");			
		}
		if (vId.matches(XRI_GLOBAL_CONTEXT_REGEX)) {
			vIsXri = true;
		}

		return vIsXri;
	}

	public boolean isXRI() {
		return iIsXri;
	}

	public String toString() {
		return iId;
	}

	public URL getURL() {
		return iUrl;
	}

	public static Identifier create(String aId) throws IdetifierException {
		try {
			return new Identifier(normalize(aId), isXRI(aId));
		} catch (MalformedURLException e) {
			throw new IdetifierException("Malformed URL", e);
		}
	}

	public String getName() {
		return iId;
	}

}
