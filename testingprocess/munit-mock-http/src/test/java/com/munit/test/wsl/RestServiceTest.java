package com.munit.test.wsl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.transport.PropertyScope;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

import com.munit.test.common.RequestMessageBuilder;
import com.wsl.rest.common.ResponseJsonObject;
import com.wsl.rest.common.ResponseXmlObject;
/**
 * @author Mohammad rafiq
 * 
 */
public class RestServiceTest extends FunctionalMunitSuite {

	
	/**
	 * 
	 * @return The location of your MULE flow file.
	 */
	@Override
	protected String getConfigResources() {
		return "munitMockHTTP-REST.xml";
	}

	/**
	 * Test-case: should mock HTTP outbound-endpoint with following
	 * 1. address = http://localhost:8686/ActualRestServer/RestServerClass?param1=Team
	 * 2. http.method = GET
	 * 
	 * when mocked it should return payload as 
	 * 	<?xml version="1.0" encoding="UTF-8"?>
	 * 	<data>
	 * 		<company>WSL</company>
	 * 		<name>Team mock</name>
	 * 	</data>
	 * 3. assert not null
	 * 4. assert equal between the mocked message and obtained flow response.
	 * 5. assert class name not same.
	 * 		--> Mock JSON response will return object of class org.json.simple.JSONObject,
	 * 		--> So check with class org.mule.module.json.JsonData   
	 */
	@Test
	public void testMockRESTEndpointForGET() throws Exception {

		//mocking the endpoint with URL
		//ResponseXmlObject.getXMLData("Team mock") is the method which set message properties to GET
		whenEndpointWithAddress("http://localhost:8686/ActualRestServer/RestServerClass?param1=Team")
				.thenReturn(muleMessageWithPayload(ResponseXmlObject.getXMLData("Team mock")));
		
		//invokes flow munit-http-RestConsumer
		//new RequestMessageBuilder().restInputMessageForGETOperation(muleContext) is a method which set message properties to GET

		MuleEvent resultEvent = runFlow("munit-http-RestConsumer",
				testEvent(new RequestMessageBuilder().restInputMessageForGETOperation(muleContext)));
		
		assertNotNull(resultEvent.getMessage().getPayload());
		assertEquals(ResponseXmlObject.getXMLData("Team mock"), resultEvent.getMessage().getPayload());
		assertNotSame("class java.lang.Object",resultEvent.getMessage().getPayload().getClass().toString());
	}
	
	
	/**
	 * Test-case: should mock HTTP outbound-endpoint with following
	 * 1. address = http://localhost:8686/ActualRestServer/RestServerClass?param1=Team
	 * 2. http.method = POST
	 * 
	 * when mocked it should return JSON payload as
	 * {"balance":1000.21,"num":100,"is_vip":true,"name":"Team mock"}
	 * 
	 * 3. assert not null
	 * 4. assert equal between the mocked message and obtained flow response.
	 * 5. assert class name not same.
	 * 		--> Mock JSON response will return object of class org.json.simple.JSONObject,
	 * 		--> So check with class org.mule.module.json.JsonData   
	 */
	@Test
	public void testMockRESTEndpointForPOST() throws Exception {
		//mocking the endpoint with URL
		whenEndpointWithAddress("http://localhost:8686/ActualRestServer/RestServerClass?param1=Team")
				.thenReturn(muleMessageWithPayload(ResponseJsonObject.getJSONData("Team mock")));
		//invokes flow 'munit-http-RestConsumer'
		//new RequestMessageBuilder().restInputMessageForPOSTOperation(muleContext) is a method which set message properties to POST
		MuleEvent resultEvent = runFlow("munit-http-RestConsumer",
				testEvent(new RequestMessageBuilder().restInputMessageForPOSTOperation(muleContext)));

		assertNotNull(resultEvent.getMessage().getPayload());
		assertEquals(ResponseJsonObject.getJSONData("Team mock"), resultEvent.getMessage().getPayload());
		assertNotSame("class org.mule.module.json.JsonData",resultEvent.getMessage().getPayload().getClass().toString());
		
	}
	

}