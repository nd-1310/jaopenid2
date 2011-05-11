package com.apetrenko.jaopenid.messages;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class ResponseParser {

	private static final String SPLITTER = ":";
	private static final String NEWLINE = "\n";

	public static DirectResponse parseDirect(String aRawResponse) {

		Map<String, String> vParams = getAsMap(aRawResponse);

		// TODO Replace strings with constants.
		DirectResponse vResponse = null;
		if (vParams.get("mac_key") != null) {
			vResponse = new AssociationResponse();
		} else if (vParams.get("enc_mac_key") != null) {
			vResponse = new EncAssocResponse();
		} else if (vParams.get("error_code").equals("unsupported-type")) {
			vResponse = new AssociationError();
		} else if (vParams.get("error") != null) {
			vResponse = new ErrorResponse();
		}

		vResponse.setParameters(vParams);

		return vResponse;
	}

	private static Map<String, String> getAsMap(String aRaw) {
		Map<String, String> vResult = new Hashtable<String, String>();
		String[] vItems = aRaw.split(NEWLINE);
		for (int i = 0; i < vItems.length; i++) {
			int vPos = vItems[i].indexOf(SPLITTER);
			String vKey = vItems[i].substring(0, vPos);
			String vValue = vItems[i].substring(vPos + 1);
			vResult.put(vKey, vValue);
		}
		return vResult;
	}

	private static Map<String, String> getAsMap(Map<String, String[]> aParams) {
		Map<String, String> vResult = new HashMap<String, String>();
		Set<String> vKeys = aParams.keySet();
		String[] vEntry = null;
		String vValue = null;
		for (String vKey : vKeys) {
			vEntry = aParams.get(vKey);
			if (vEntry.length == 1) {
				vValue = vEntry[0];
				vResult.put(vKey, vValue);
			}
		}
		return vResult;
	}

	public static IndirectResponse parseIndirect(Map<String, String[]> aParams) {
		Map<String, String> vParams = getAsMap(aParams);

		// TODO Replace strings with constants.
		IndirectResponse vResponse = null;
		if (vParams.get("openid.mode").equals("id_res")) {
			vResponse = new AuthenticationResponse();
		} else if (vParams.get("openid.mode").equals("setup_needed")) {
			vResponse = new AuthenticationError();
		} else if (vParams.get("openid.mode").equals("cancel")) {
			vResponse = new AuthenticationError();
		}

		vResponse.setParameters(vParams);

		return vResponse;
	}

}
