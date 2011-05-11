package com.apetrenko.jaopenid;

import java.net.URL;

import com.apetrenko.jaopenid.consumer.OpenIDConsumer;
import com.apetrenko.jaopenid.exceptions.HttpException;
import com.apetrenko.jaopenid.external.ConsumerStore;
import com.apetrenko.jaopenid.external.HttpConnector;
import com.apetrenko.jaopenid.messages.DirectRequest;
import com.apetrenko.jaopenid.messages.DirectResponse;
import com.apetrenko.jaopenid.messages.ResponseParser;

public class OpenIDManager {

	private HttpConnector iConnector;

	public void close() {
		iConnector.close();
	}

	public static OpenIDManager create(HttpConnector aConnector) {
		OpenIDManager vResult = new OpenIDManager();
		vResult.iConnector = aConnector;
		return vResult;
	}

	public DirectResponse sendMessage(URL aUrl, DirectRequest aReq) throws HttpException {		
		String vRawResponse = iConnector.post(aUrl, aReq.getParameterMap());
		DirectResponse vResult = ResponseParser.parseDirect(vRawResponse);
		
		return vResult;
	}

	public OpenIDConsumer createConsumer(ConsumerStore aStore){
		return OpenIDConsumer.create(this, aStore);
	}

	public HttpConnector getHttpConnector() {
		return iConnector;
	}

}
