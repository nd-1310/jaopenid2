package com.apetrenko.jaopenid.messages;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import com.apetrenko.jaopenid.exceptions.OpenIDRuntimeException;

/**
 * <code>IndirectRequest</code> class represents OpenID indirect request (see OpenID 2.0 specification, chapter 5.2).
 * 
 * @author a.petrenko
 *
 */
public abstract class IndirectRequest extends Request {
	
	public final URL getUrl(URL aTarget){
		Map<String, String> vParams = getParameterMap();
		String vRequest = aTarget.getQuery();
		if (vRequest == null){
			vRequest = "";
		}
		for (String vKey : vParams.keySet()) {
			vRequest = encodeParameter(vRequest, vKey, vParams.get(vKey));
		}
		URL vResult = null;
		try {
			vResult = new URL(aTarget.toExternalForm() + vRequest);
		} catch (MalformedURLException e) {
			throw new OpenIDRuntimeException("Cannot create URL", e);
		}
		return vResult;
	}	

	private String encodeParameter(String aPrefix, String aKey, String aValue) {
		String vResult = aPrefix;
		if (aPrefix.equalsIgnoreCase("")) {
			vResult = vResult + "?";
		} else {
			vResult = vResult + "&";
		}
		try {
			vResult = vResult + aKey + "=" + URLEncoder.encode(aValue, "UTF8");
		} catch (UnsupportedEncodingException e) {
		}

		return vResult;
	}

}
