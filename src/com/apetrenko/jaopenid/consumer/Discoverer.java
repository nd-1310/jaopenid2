package com.apetrenko.jaopenid.consumer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.apetrenko.jaopenid.entity.Identifier;
import com.apetrenko.jaopenid.entity.Service;
import com.apetrenko.jaopenid.exceptions.DiscoveryException;
import com.apetrenko.jaopenid.exceptions.HttpException;
import com.apetrenko.jaopenid.external.HttpConnector;

public class Discoverer {

	private static final String XRD_PRIORITY = "priority";
	private static final String XRD = "XRD";
	private static final String XRD_SERVICE = "Service";
	private static final String XRD_TYPE = "Type";
	private static final String XRD_URI = "URI";
	private static final String XRD_LOCAL_ID = "LocalID";
	private static final CharSequence MIME_XRDS = "application/xrds+xml";
	private static final String XRI_PROXY = "http://xri.net/";

	private HttpConnector iHttpConnector;

	public Discoverer(HttpConnector aHttpConnector) {
		iHttpConnector = aHttpConnector;
	}

	private List<Service> processXrds(String aXrds) throws DiscoveryException {
		if (aXrds == null)
			throw new DiscoveryException("XRD is null");
		List<Service> vResult = new LinkedList<Service>();
		try {
			InputStream is = new ByteArrayInputStream(aXrds.getBytes());

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document document = docBuilder.parse(is);
			NodeList vXrdNodes = document.getElementsByTagName(XRD);

			int vLength = vXrdNodes.getLength();
			if (vLength < 1) {
				throw new DiscoveryException("No XRD found");
			}
			// Get last XRDS element
			Node vXrdNode = vXrdNodes.item(vLength - 1);
			NodeList vXrdChildren = vXrdNode.getChildNodes();

			Service vService = null;
			for (int i = 0; i < vXrdChildren.getLength(); i++) {
				Node vXrdChild = vXrdChildren.item(i);
				if (XRD_SERVICE.equals(vXrdChild.getNodeName())) {
					vService = createServiceFromNode(vXrdChild);
					if (vService != null) {
						vResult.add(vService);
					}
				}
			}
		} catch (Exception vE) {
			throw new DiscoveryException("Exception while processing XRD", vE);
		}
		return vResult;
	}

	private Service createServiceFromNode(Node aNode) {
		Service vResult = new Service();
		Node vPriorityNode = aNode.getAttributes().getNamedItem(XRD_PRIORITY);
		int vPr = 0;
		if (vPriorityNode != null) {
			vPr = Integer.parseInt(vPriorityNode.getNodeValue());
			vResult.setPriority(vPr);
		}
		NodeList vChildren = aNode.getChildNodes();
		for (int i = 0; i < vChildren.getLength(); i++) {
			Node vChild = vChildren.item(i);
			if (XRD_URI.equals(vChild.getNodeName())) {
				vPriorityNode = vChild.getAttributes().getNamedItem(
						XRD_PRIORITY);
				if (vPriorityNode != null) {
					vPr = Integer.parseInt(vPriorityNode.getNodeValue());
					vResult.setURI(vChild.getTextContent(), vPr);
				} else {
					vResult.setURI(vChild.getTextContent(), 0);
				}
			} else if (XRD_LOCAL_ID.equals(vChild.getNodeName())) {
				vResult.setIdentity(vChild.getTextContent());
			} else if (XRD_TYPE.equals(vChild.getNodeName())) {
				vResult.setType(vChild.getTextContent());
			}
		}
		return vResult;
	}

	public List<Service> discover(Identifier aId) throws DiscoveryException {
		String vXrds = null;
		List<Service> vResult = null;
		try {
			if (aId.isXRI()) {
				vXrds = tryXriDiscovery(aId);
			} else {
				vXrds = tryYadisDiscovery(aId);
			}
			vResult = processXrds(vXrds);
		} catch (HttpException e) {
			// Ignore
		} finally {
			if ((vResult == null) && (!aId.isXRI())) {
				vResult = tryHtmlDiscovery(aId);
			}
		}
		if (vResult == null){
			vResult = new LinkedList<Service>();
		}
		return vResult;
	}

	private List<Service> tryHtmlDiscovery(Identifier aId)
			throws DiscoveryException {
		// TODO Implement HTML Discovery
		//throw new DiscoveryException("HTML discovery is not implemented yet");
		return null;
	}

	private String tryYadisDiscovery(Identifier aId) throws DiscoveryException,
			HttpException {
		String vResult = null;
		Map<String, String> vProps = new HashMap<String, String>();
		Map<String, String> vHeaders = iHttpConnector
				.head(aId.getURL(), vProps);
		String vLocation = vHeaders.get("X-XRDS-Location");
		if (vLocation == null) {
			if (vHeaders.get("Content-Type").contains(MIME_XRDS)) {
				vProps.clear();
				vResult = iHttpConnector.get(aId.getURL(), vProps);
			}
		} else {
			vResult = getXrds(vLocation);
		}
		return vResult;
	}

	private String getXrds(String aXrdsUrl) throws DiscoveryException {
		try {
			return iHttpConnector.get(new URL(aXrdsUrl));
		} catch (MalformedURLException e) {
			throw new DiscoveryException("Bad XRDS URI", e);
		} catch (HttpException e) {
			throw new DiscoveryException("HTTP Error", e);
		}
	}

	private String tryXriDiscovery(Identifier aId) throws DiscoveryException {

		// TODO Implement XRI Discovery
		try {
			URL vUrl = new URL(XRI_PROXY + aId.getName());
			String vResult = iHttpConnector.get(vUrl);
			return vResult;
		} catch (Exception vE) {
			throw new DiscoveryException("XRI discovery error", vE);
		}

		// throw new DiscoveryException("XRI discovery is not implemented yet");
	}

	public void close() {
		// TODO Auto-generated method stub

	}
	
	public static final Discoverer create(HttpConnector aHttpConnector){
		return null;
	}

}