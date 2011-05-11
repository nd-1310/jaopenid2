package com.apetrenko.jaopenid.messages;


public class EncAssocResponse extends AssociationResponse {

	public String getServerPublicKey() {
		return getParameter("dh_server_public");
	}

	public String getEncodedMac() {
		return getParameter("enc_mac_key");
	}
	
}
