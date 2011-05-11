package com.apetrenko.jaopenid.crypto;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.apetrenko.jaopenid.exceptions.OpenIDRuntimeException;

public class Crypto {

	private static final String HMAC_SHA1 = "HMACSHA1";
	private static final String HMAC_SHA256 = "HMACSHA256";
	private static final String SHA1 = "SHA-1";
	private static final String SHA256 = "SHA-256";	
	
	private static MessageDigest sha1;
	private static MessageDigest sha256;
	static {
		try {
			sha1 = MessageDigest.getInstance(SHA1);
			sha256 = MessageDigest.getInstance(SHA256);
		} catch (NoSuchAlgorithmException e) {
			throw new OpenIDRuntimeException(e.getLocalizedMessage());
		}
	}

	public static String encode(BigInteger aData) {
		return encode(aData.toByteArray());
	}

	public static String encode(byte[] aData) {
		return Base64.encodeToString(aData, false);
	}

	public static BigInteger decodeBigInteger(String aS) {
		return new BigInteger(decode(aS));
	}

	public static byte[] decode(String aData) {
		return Base64.decodeFast(aData);
	}


	public static byte[] sha256(byte[] aByteArray) {
		return sha256.digest(aByteArray);
	}

	public static byte[] sha1(byte[] aByteArray) {
		return sha1.digest(aByteArray);
	}
	
	/**
     * Signs a message using HMAC SHA1.
     *
     * @param aKey the key to sign with.
     * @param aText the bytes to sign.
     * @return the signed bytes.
     * @throws InvalidKeyException if <code>key</code> is not a good HMAC key.
     * @throws NoSuchAlgorithmException if HMACSHA1 is not available.
     */
    public static byte[] hmacSha1(byte[] aKey, byte[] aText)
        throws InvalidKeyException, NoSuchAlgorithmException
    {
        return hmacShaX(HMAC_SHA1, aKey, aText);
    }

    /**
     * Signs a message using HMAC SHA256.
     *
     * @param aKey the key to sign with.
     * @param aText the bytes to sign.
     * @return the signed bytes.
     * @throws InvalidKeyException if <code>key</code> is not a good HMAC key.
     * @throws NoSuchAlgorithmException if HMACSHA1 is not available.
     */
    public static byte[] hmacSha256(byte[] aKey, byte[] aText)
        throws InvalidKeyException, NoSuchAlgorithmException
    {
        return hmacShaX(HMAC_SHA256, aKey, aText);
    }

    private static byte[] hmacShaX(String aAlgorithm, byte[] aKey, byte[] aText)
        throws InvalidKeyException, NoSuchAlgorithmException
    {
        SecretKey vSecretKey = new SecretKeySpec(aKey, aAlgorithm);
        Mac vMac = Mac.getInstance(vSecretKey.getAlgorithm());
        vMac.init(vSecretKey);
        return vMac.doFinal(aText);
    }


}
