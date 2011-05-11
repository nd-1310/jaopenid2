package com.apetrenko.jaopenid.external;

import java.net.URL;
import java.util.Map;

import com.apetrenko.jaopenid.exceptions.HttpException;


public interface HttpConnector {
		
	public void close();
	
	public String get(URL aURL) throws HttpException;
	public String get(URL aURL, Map<String, String> aParams) throws HttpException;
	public String get(URL aURL, String aMimeType, Map<String, String> aParams) throws HttpException;
	
	public Map<String, String> head(URL aUrl) throws HttpException;
	public Map<String, String> head(URL aUrl, Map<String, String> aProps) throws HttpException;
	
	public String post(URL aUrl, Map<String, String> aParams) throws HttpException;

}
