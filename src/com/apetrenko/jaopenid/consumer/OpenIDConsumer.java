package com.apetrenko.jaopenid.consumer;

import java.math.BigInteger;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.apetrenko.jaopenid.OpenIDManager;
import com.apetrenko.jaopenid.crypto.Crypto;
import com.apetrenko.jaopenid.crypto.DHKeyPair;
import com.apetrenko.jaopenid.crypto.DiffieHellman;
import com.apetrenko.jaopenid.entity.Association;
import com.apetrenko.jaopenid.entity.Identifier;
import com.apetrenko.jaopenid.entity.Nonce;
import com.apetrenko.jaopenid.entity.Service;
import com.apetrenko.jaopenid.exceptions.DiscoveryException;
import com.apetrenko.jaopenid.exceptions.HttpException;
import com.apetrenko.jaopenid.exceptions.OpenIDException;
import com.apetrenko.jaopenid.external.ConsumerStore;
import com.apetrenko.jaopenid.messages.AssociationError;
import com.apetrenko.jaopenid.messages.AssociationRequest;
import com.apetrenko.jaopenid.messages.AssociationResponse;
import com.apetrenko.jaopenid.messages.AuthenticationError;
import com.apetrenko.jaopenid.messages.AuthenticationRequest;
import com.apetrenko.jaopenid.messages.AuthenticationResponse;
import com.apetrenko.jaopenid.messages.EncAssocResponse;
import com.apetrenko.jaopenid.messages.ErrorResponse;
import com.apetrenko.jaopenid.messages.IndirectResponse;
import com.apetrenko.jaopenid.messages.Message;
import com.apetrenko.jaopenid.messages.Response;
import com.apetrenko.jaopenid.messages.ResponseParser;

public class OpenIDConsumer {

	private Discoverer iDiscoverer;
	private OpenIDManager iManager;
	private ConsumerStore iStore;
	private int iNonceTtl;

	private OpenIDConsumer() {
		// TODO Remove hardcoded nonce TTL.
		iNonceTtl = 60;
	}

	private AuthStatus verifyResponse(AuthenticationResponse aResponse) {
		boolean vIsValid = true;

		if (!verifyNonce(aResponse)) {
			vIsValid = false;
		} else if (!verifySignature(aResponse)) {
			vIsValid = false;
		}

		AuthStatus vResult = null;
		if (vIsValid) {
			vResult = new AuthStatus(aResponse.getIdentity(),
					aResponse.getProvider());
		} else {
			vResult = new AuthStatus(AuthStatus.ERROR);
		}
		return vResult;
	}

	private boolean verifyNonce(AuthenticationResponse aResponse) {
		Nonce vNonce = Nonce.create(aResponse.getNonce());
		if (System.currentTimeMillis() - vNonce.getTimestamp() > iNonceTtl * 1000) {
			return false;
		} else if (iStore.isSeen(vNonce)) {
			return false;
		}
		return true;
	}

	public AuthStatus verifyResponse(Map<String, String[]> aParameters) {
		IndirectResponse vResp = ResponseParser.parseIndirect(aParameters);
		if (vResp instanceof AuthenticationResponse) {
			return verifyResponse((AuthenticationResponse) vResp);
		} else if (vResp instanceof AuthenticationError) {
			// User canceled request.
			AuthStatus vResult = new AuthStatus(AuthStatus.CANCEL);
			return vResult;
		}
		return null;
	}

	public void close() {
		iDiscoverer.close();
		// TODO: Add OpenIDManager.close().
	}

	public URL getAuthenticationUrl(String aId, URL aReturnTo, String aRealm)
			throws OpenIDException {
		return getAuthenticationUrl(Identifier.create(aId), aReturnTo, aRealm);
	}

	public URL getAuthenticationUrl(Identifier aId, URL aReturnTo, String aRealm)
			throws OpenIDException {
		Association vAssoc = getAssociation(aId);

		AuthenticationRequest vAuthReq = new AuthenticationRequest();
		vAuthReq.setHandle(vAssoc.getHandle());
		vAuthReq.setVersion(Message.VERSION_2_0);

		// TODO Add support of IMMEDIATE requests
		vAuthReq.setMode(AuthenticationRequest.SETUP);
		vAuthReq.setClaimedId(vAssoc.getLocalId());
		vAuthReq.setIdentity(vAssoc.getIdentity());
		vAuthReq.setRealm(aRealm);
		vAuthReq.setReturnTo(aReturnTo);

		return vAuthReq.getUrl(vAssoc.getURL());
	}

	private Association getAssociation(Identifier aId) throws OpenIDException {
		Association vResult = iStore.getAssociation(aId);

		if ((vResult == null) || (!vResult.isValid())) {
			vResult = createAssociation(aId);
		}

		return vResult;
	}

	private Association createAssociation(Identifier aId)
			throws OpenIDException {
		Association vResult = null;

		List<Service> vServices = getServicesList(aId);
		for (Service vService : vServices) {
			try {
				vResult = doAssociate(vService);
			} catch (HttpException vE) {
				continue;
			}
			if (vResult != null) {
				break;
			}
		}
		if (vResult == null) {
			// TODO Implement stateless mode
			throw new OpenIDException("Could not create association for ID: "
					+ aId.toString());
		} else {
			iStore.put(aId, vResult);
		}
		return vResult;
	}

	private Association doAssociate(Service aService) throws HttpException {
		Association vResult = null;

		AssociationRequest vReq = new AssociationRequest();

		BigInteger vMac = null;
		BigInteger vKey = null;

		if (aService.isSecure()) {
			vReq.setAssocType(AssociationRequest.HMAC_SHA256);
			vReq.setSessionType(AssociationRequest.DH_NO_ENCRYPT);
		} else {
			vReq.setAssocType(AssociationRequest.HMAC_SHA256);
			vReq.setSessionType(AssociationRequest.DH_SHA256);

			DHKeyPair vKeyPair = DiffieHellman.getKeyPair();
			String vPubKey = Crypto.encode(vKeyPair.getPublicKey());
			vKey = vKeyPair.getPrivateKey();

			vReq.setDhPublic(vPubKey);
		}

		Response vResp = iManager.sendMessage(aService.getUrl(), vReq);

		if (vResp instanceof EncAssocResponse) {
			EncAssocResponse vEncResp = (EncAssocResponse) vResp;
			BigInteger vServerPublic = Crypto.decodeBigInteger(vEncResp
					.getServerPublicKey());
			BigInteger vShared = DiffieHellman.getSharedSecret(vServerPublic,
					vKey);
			byte[] vMacBytes = DiffieHellman.xorSecret(vShared,
					vEncResp.getEncodedMac());
			vMac = new BigInteger(vMacBytes);

			// TODO Ugly code - refactor.
			vResult = new Association(vEncResp.getHandle(), vMac,
					vEncResp.getTtl(), aService.getClaimedId(),
					aService.getUrl());

		} else if (vResp instanceof AssociationResponse) {
			AssociationResponse vAssocResp = (AssociationResponse) vResp;
			vMac = Crypto.decodeBigInteger(vAssocResp.getMac());

			vResult = new Association(vAssocResp.getHandle(), vMac,
					vAssocResp.getTtl(), aService.getClaimedId(),
					aService.getUrl());

		} else if (vResp instanceof AssociationError) {

		} else if (vResp instanceof ErrorResponse) {

		}

		return vResult;
	}

	private List<Service> getServicesList(Identifier aId)
			throws DiscoveryException {
		return discoverServices(aId);
	}

	private List<Service> discoverServices(Identifier aId)
			throws DiscoveryException {
		return iDiscoverer.discover(aId);
	}

	private boolean verifySignature(AuthenticationResponse aResp) {
		if (aResp.verifySigned()) {
			Association vAssoc = iStore.getAssociation(aResp.getAssoc());
			byte[] vSignature = aResp.sign(vAssoc.getMac());

			BigInteger vServerSig = Crypto.decodeBigInteger(aResp
					.getSignature());
			BigInteger vClientSig = new BigInteger(vSignature);

			if (vServerSig.equals(vClientSig)) {
				return true;
			}
		}
		return false;
	}

	public static OpenIDConsumer create(OpenIDManager aManager,
			ConsumerStore aStore) {
		OpenIDConsumer vConsumer = new OpenIDConsumer();
		vConsumer.iManager = aManager;
		vConsumer.iStore = aStore;
		vConsumer.iDiscoverer = new Discoverer(aManager.getHttpConnector());
		return vConsumer;
	}

}
