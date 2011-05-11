package com.apetrenko.jaopenid.messages;

import java.util.Map;


/**
 * <code>DirectRequest</code> class represents OpenID direct request (see <a href="http://openid.net/specs/openid-authentication-2_0.html#direct_comm">OpenID 2.0 specification, chapter 5.1</a>).
 * 
 * @author a.petrenko
 *
 */
public abstract class DirectRequest extends Request {
	
	@Override
	public final Map<String, String> getParameterMap(){
		return super.getParameterMap();
	}

}
