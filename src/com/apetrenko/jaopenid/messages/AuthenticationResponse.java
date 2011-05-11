package com.apetrenko.jaopenid.messages;

import java.math.BigInteger;
import java.util.StringTokenizer;

import com.apetrenko.jaopenid.OpenIDConsts;
import com.apetrenko.jaopenid.crypto.Crypto;
import com.apetrenko.jaopenid.exceptions.OpenIDRuntimeException;

public class AuthenticationResponse extends IndirectResponse {

	private static final String SPLITTER = ",";
	private static final String OPENID_PREFIX = "openid.";
	private static final String UTF = "UTF-8";
	private static final char SEPARATOR = ':';
	private static final char NEWLINE = '\n';

	@Override
	public boolean isOk() {
		return true;
	}

	public byte[] sign(BigInteger aKey) {
		String vSigned = getParameter(OpenIDConsts.SIGNED);
		StringTokenizer st = new StringTokenizer(vSigned, SPLITTER);
		StringBuffer sb = new StringBuffer();
		while (st.hasMoreTokens()) {
			String s = st.nextToken();
			String name = OPENID_PREFIX + s;
			String value = (String) getParameter(name);

			sb.append(s);
			sb.append(SEPARATOR);
			sb.append(value);
			sb.append(NEWLINE);
		}
		byte[] vToSign = null;
		byte[] vResult = null;
		try {
			vToSign = sb.toString().getBytes(UTF);
			vResult = Crypto.hmacSha256(aKey.toByteArray(), vToSign);
		} catch (Exception e) {
			throw new OpenIDRuntimeException(e);
		}
		return vResult;
	}

	public String getAssoc() {
		return getParameter(OpenIDConsts.ASSOC_HANDLE);
	}

	public String getSignature() {
		return getParameter(OpenIDConsts.SIG);
	}

	public boolean verifySigned() {
		// TODO Add signed list verification.
		return true;
	}

	public String getNonce() {
		return getParameter(OpenIDConsts.NONCE);
	}

	public String getIdentity() {
		return getParameter(OpenIDConsts.IDENTITY);
	}

	public String getProvider() {
		return getParameter(OpenIDConsts.ENDPOINT);
	}

}
