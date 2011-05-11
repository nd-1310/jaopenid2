package com.apetrenko.jaopenid.crypto;

import java.math.BigInteger;

public class DHKeyPair {

	private final BigInteger iPrivateKey;
	private final BigInteger iPublicKey;

	public DHKeyPair(BigInteger aPublicKey, BigInteger aPrivateKey) {
		iPublicKey = aPublicKey;
		iPrivateKey = aPrivateKey;
	}

	public BigInteger getPrivateKey() {
		return iPrivateKey;
	}

	public BigInteger getPublicKey() {
		return iPublicKey;
	}

}
