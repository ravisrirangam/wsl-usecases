
/**
 * @author Mohammad rafiq
 * 
 */
package com.wsl.soap.common;

public class RequestXMLObject {
	/**
	 * @return
	 * 		<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:flig="http://www.FlightInfomation.org">
	 *			<soapenv:Header/>
	 *			<soapenv:Body>
	 *   			<flig:Request>
	 *     				<flig:destination>Sydney</flig:destination>
	 *				</flig:Request>
	 *			</soapenv:Body>
	 *		</soapenv:Envelope>
	 */
	public static Object getSOAPRequest() {
		String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:flig=\"http://www.FlightInfomation.org\">"
				+ "<soapenv:Header/>"
				+ "<soapenv:Body>"
				+ "<flig:Request>"
				+ "<flig:destination>Sydney</flig:destination>"
				+ "</flig:Request>" + "</soapenv:Body>" + "</soapenv:Envelope>";

		return request;
	}
}


