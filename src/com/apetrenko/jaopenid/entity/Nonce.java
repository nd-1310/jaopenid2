package com.apetrenko.jaopenid.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.apetrenko.jaopenid.exceptions.OpenIDRuntimeException;

public final class Nonce implements Serializable {

	private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	private static SimpleDateFormat iDateFormat;
	static {
		iDateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.US);
		iDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	private static final long serialVersionUID = 352899165016542249L;

	private long iTimestamp;
	private String iNonce;

	private Nonce() {
	}

	private Nonce(long aTimestamp, String aNonce) {
		iTimestamp = aTimestamp;
		iNonce = aNonce;
	}

	public static final Nonce create(String aNonce) {
		try {
			Date vDate = iDateFormat.parse(aNonce);
			Nonce vResult = new Nonce(vDate.getTime(), aNonce);
			return vResult;
		} catch (Exception vE) {
			throw new OpenIDRuntimeException("Invalid nonce string", vE);
		}
	}

	public long getTimestamp() {
		return iTimestamp;
	}
	
	public String getNonce(){
		return iNonce;
	}

}
