package com.apetrenko.jaopenid.messages;


public class AssociationError extends AssociationResponse {

	@Override
	public boolean isOk() {
		return false;
	}

}
