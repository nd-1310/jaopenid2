package com.apetrenko.jaopenid;

public interface OpenIDConsts {

	public static final String MESSAGE_V_10 = "http://openid.net/signon/1.0";
	public static final String MESSAGE_V_11 = "http://openid.net/signon/1.1";
	public static final String MESSAGE_V_20 = "http://specs.openid.net/auth/2.0";

	public static final String ASSOCIATE = "associate";

	public static final String MODE_IMMEDIATE = "checkid_immediate";
	public static final String MODE_SETUP = "checkid_setup";
	public static final String HANDLE = "openid.assoc_handle";
	public static final String CLAIMED_ID = "openid.claimed_id";
	public static final String IDENTITY = "openid.identity";
	public static final String RETURN_TO = "openid.return_to";
	public static final String REALM = "openid.realm";
	public static final String NS = "openid.ns";
	public static final String MODE = "openid.mode";

	public static final String SESSION_TYPE = "openid.session_type";

	public static final String SESSION_TYPE_NONE = "no-encryption";
	public static final String SESSION_TYPE_SHA1 = "DH-SHA1";
	public static final String SESSION_TYPE_SHA256 = "DH-SHA256";
	public static final String DH_PUBLIC = "openid.dh_consumer_public";

	public static final String ASSOC_TYPE = "openid.assoc_type";

	public static final String ASSOC_TYPE_SHA256 = "HMAC-SHA256";
	public static final String ASSOC_TYPE_SHA1 = "HMAC-SHA1";
	public static final String ASSOC_HANDLE = "openid.assoc_handle";
	public static final String SIGNED = "openid.signed";
	public static final String SIG = "openid.sig";
	public static final String NONCE = "openid.response_nonce";
	public static final String ENDPOINT = "openid.op_endpoint";

}
