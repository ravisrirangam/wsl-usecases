package com.wsl.rest.common;

/**
 * @author Mohammad rafiq
 * 
 */
public class ResponseXmlObject {
	/**
	 * @param param1
	 * @return xmlString <?xml version="1.0" encoding="UTF-8"?><data><company>WSL</company><name>Team mock</name></data>
	 */
	public static Object getXMLData(String param1) {

		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<data>" 
					+ "<company>WSL</company>"
					+ "<name>"+param1+"</name>"
				+ "</data>" ;
		return xmlString;

	}
}
