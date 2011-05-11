package com.apetrenko.jaopenid.crypto;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.apetrenko.jaopenid.exceptions.OpenIDRuntimeException;


public class DiffieHellman {

	private static SecureRandom iRandom;
	static {
		try {
			iRandom = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			throw new OpenIDRuntimeException("No secure random available!");
		}
	}

	public static final BigInteger DEFAULT_MODULUS = new BigInteger(
					  "1551728981814736974712322577637155399157248019669"
					+ "154044797077953140576293785419175806512274236981"
					+ "889937278161526466314385615958256881888899512721"
					+ "588426754199503412587065565498035801048705376814"
					+ "767265132557470407658574792912915723345106432450"
					+ "947150072296210941943497839259847603755949858482"
					+ "53359305585439638443");

	/**
	 * The default generator defined in the specification.
	 */
	public static final BigInteger DEFAULT_GENERATOR = BigInteger.valueOf(2);

	public static DHKeyPair getKeyPair(BigInteger mod, BigInteger gen) {
		DHKeyPair vResult = null;

		BigInteger modulus = (mod != null ? mod : DiffieHellman.DEFAULT_MODULUS);
		BigInteger generator = (gen != null ? gen
				: DiffieHellman.DEFAULT_GENERATOR);

		int bits = modulus.bitLength();
		BigInteger max = modulus.subtract(BigInteger.ONE);
		while (true) {
			BigInteger pkey = new BigInteger(bits, iRandom);
			if (pkey.compareTo(max) >= 0) { // too large
				continue;
			} else if (pkey.compareTo(BigInteger.ONE) <= 0) {// too small
				continue;
			}
			BigInteger privateKey = pkey;
			BigInteger publicKey = generator.modPow(privateKey, modulus);
			vResult = new DHKeyPair(publicKey, privateKey);
			break;
		}
		return vResult;
	}

	public static BigInteger getSharedSecret(BigInteger aModulus,
			BigInteger aOtherPublicKey, BigInteger aPrivateKey) {
		return aOtherPublicKey.modPow(aPrivateKey, aModulus);
	}

	public static BigInteger getSharedSecret(BigInteger aOtherPublicKey,
			BigInteger aPrivateKey) {
		return aOtherPublicKey.modPow(aPrivateKey, DEFAULT_MODULUS);
	}

	public static byte[] xorSecret(BigInteger aSharedKey,
			String aSecret) {
		if (aSharedKey == null) {
			throw new IllegalArgumentException("Shared key cannot be null");
		}

		byte[] vSecret = Crypto.decode(aSecret);

		byte[] vHashed = null;
		if (vSecret.length == 32) {
			vHashed = Crypto.sha256(aSharedKey.toByteArray());
		} else {
			vHashed = Crypto.sha1(aSharedKey.toByteArray());
		}

		if (vSecret.length != vHashed.length) {

			throw new RuntimeException("nyi");
		}

		byte[] result = new byte[vSecret.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = (byte) (vHashed[i] ^ vSecret[i]);
		}
		return result;
	}

	public static DHKeyPair getKeyPair() {
		return getKeyPair(null, null);
	}

}
