package com.wsl.soap.common;
/**
 * @author Mohammad rafiq
 * 
 */
public class ResponseXMLObject {

	/**
 	* @return
 	*<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
	*   <soap:Body>
	*      <Response xmlns="http://www.FlightInfomation.org">
	*         <fareWithAirlines>Sydney String Appended.</fareWithAirlines>
	*      </Response>
	*   </soap:Body>
	*</soap:Envelope>
	*
	*/
	public static Object getSOAPResponse() {
		String response = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>"
				+ "<Response xmlns=\"http://www.FlightInfomation.org\">"
				+ "<fareWithAirlines>Sydney String Appended.</fareWithAirlines>"
				+ "</Response>" + "</soap:Body>" + "</soap:Envelope>";
		return response;

	}
}
