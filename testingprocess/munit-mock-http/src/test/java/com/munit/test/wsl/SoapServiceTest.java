package com.munit.test.wsl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

import com.wsl.soap.common.RequestXMLObject;
import com.wsl.soap.common.ResponseXMLObject;
/**
 * @author Mohammad rafiq
 * 
 */
public class SoapServiceTest extends FunctionalMunitSuite {
	/**
	 * 
	 * @return The location of your MULE flow file.
	 */
	@Override
	protected String getConfigResources() {
		return "munitMockHTTP-SOAP.xml";
	}

	/**
	 * Test-case: should mock HTTP outbound-endpoint with following
	 * 1. address = http://localhost:8082/flightInformation
	 * when mocked it should return payload as 
	 * <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
	 *   <soap:Body>
	 *      <Response xmlns="http://www.FlightInfomation.org">
	 *         <fareWithAirlines>Sydney String Appended.</fareWithAirlines>
	 *      </Response>
	 *   </soap:Body>
	 * </soap:Envelope>
	 * 
	 * 2.Send payload as
	 * 	<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:flig="http://www.FlightInfomation.org">
	 *		<soapenv:Header/>
	 *		<soapenv:Body>
	 *  		<flig:Request>
	 *  			<flig:destination>Sydney</flig:destination>
	 *			</flig:Request>
	 *		</soapenv:Body>
	 *	</soapenv:Envelope>
	 * 
	 * 3. assert response not null
	 * 4. assert equal between the mocked message and obtained flow response.
	 * 5. assert class name not same.
	 * 		--> Mock'ed SOAP response will return object of class java.lang.String,
	 * 		--> So check with class java.lang.Object   
	 */
	@Test
	public void mockSOAPEndpointTestCase() throws Exception {

		//mocking the endpoint with URL, response using ResponseXMLObject.getSOAPResponse() method will be return as payload.
		whenEndpointWithAddress("http://localhost:8082/flightInformation").thenReturn(muleMessageWithPayload(ResponseXMLObject.getSOAPResponse()));
		
		//invokes flow munit-http-RestConsumer
		//RequestXMLObject.getSOAPRequest() is a method which set Request SOAP message 
		MuleEvent resultEvent = runFlow("munit-http-SoapConsumer",testEvent(RequestXMLObject.getSOAPRequest()));
		
		//assert response as not null. 
		assertNotNull(resultEvent.getMessage().getPayload());
		//assert for response equal between obtained and mock response.
		assertEquals(ResponseXMLObject.getSOAPResponse(), resultEvent.getMessage().getPayload());
		//assert for class not same
		assertNotSame("class java.lang.Object",resultEvent.getMessage().getPayload().getClass().toString());
	}


}
