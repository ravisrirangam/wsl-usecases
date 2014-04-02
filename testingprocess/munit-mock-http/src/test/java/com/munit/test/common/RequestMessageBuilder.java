package com.munit.test.common;

import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;

public class RequestMessageBuilder {

	/**
	 * @return a MuleMessage object with following 
	 * 
	 * in-bound properties
	 * 	param1=Team
	 * 
	 * http.method=GET 
	 * and payload 
	 * 	"input message"
	 */
	public MuleMessage restInputMessageForGETOperation(MuleContext muleContext) {
		MuleMessage message = new DefaultMuleMessage("", muleContext);

		// set http.method to GET
		message.setProperty("http.method", "GET", PropertyScope.OUTBOUND);
		// set in-bound property "param1" as "Team"
		message.setProperty("param1", "Team", PropertyScope.INBOUND);
		// set payload as input message and
		String payload = "input message";
		message.setPayload(payload);
		return message;
	}
	
	/**
	 * @return a MuleMessage object with following 
	 * 
	 * in-bound properties
	 * 	param1=Team
	 * 
	 * http.method=POST 
	 * and payload 
	 * 	"input message"
	 */
	public MuleMessage restInputMessageForPOSTOperation(MuleContext muleContext) {
		MuleMessage message = new DefaultMuleMessage("", muleContext);

		// set http.method to POST
		message.setProperty("http.method", "POST", PropertyScope.OUTBOUND);
		// set in-bound property "param1" as "Team"
		message.setProperty("param1", "Team", PropertyScope.INBOUND);
		// set payload as input message and
		String payload = "input message";
		message.setPayload(payload);
		return message;
	}
}
