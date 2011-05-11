package com.apetrenko.jaopenid.external;

import com.apetrenko.jaopenid.entity.Association;
import com.apetrenko.jaopenid.entity.Identifier;
import com.apetrenko.jaopenid.entity.Nonce;

public interface ConsumerStore {

	public void close();

	public Association getAssociation(Identifier aId);
	public Association getAssociation(String aHandle);

	public boolean isSeen(Nonce aNonce);

	public void put(Identifier aId, Association aAssoc);	
	public void put (Nonce aNonce);

}
