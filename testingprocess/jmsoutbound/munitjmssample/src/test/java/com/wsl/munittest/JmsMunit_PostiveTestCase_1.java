package com.wsl.munittest;


import java.util.HashMap;

import org.hornetq.utils.json.JSONObject;
import org.junit.Test;
import org.mortbay.util.ajax.JSON;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.module.json.JsonData;
import org.mule.munit.config.AssertNotNullMessageProcessor;
import org.mule.munit.runner.functional.FunctionalMunitSuite;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class JmsMunit_PostiveTestCase_1 extends FunctionalMunitSuite
{
	@Override
	protected String getConfigResources()
	{
        return "src/main/app/munitjmssample.xml";
    }
	
	@Test
	public void JmsMunitTest() throws Exception
	{
		
		MockJmsTest mjt= new MockJmsTest();		
		mjt.Mockjmsoutbound();
		
		/* Below line calls the flow and passes the input to the flow */
		MuleEvent response=runFlow("munitjmssampleFlow", testEvent("testing from Junit"));
		
		
		
	
		
		System.out.println("response from mule flow is"+response.getMessageAsString());
		
		/* Below condition checks whether response from mule flow is null or not */
		assertNotNull(response.getMessageAsString());
		
		/* Below is the assertion condition which check the the expected value with the response from mule flow*/
	//	assertEquals("Hornet Queue Test", response.getMessageAsString());
		
		/* Below is the condition which is used to compare in the class type of expected and actual */
		
		System.out.println(" the class type from Mule flow is "+response.getMessage().getPayload().getClass().toString());
		
	//	assertEquals("class org.hornetq.utils.json.JSONObject", response.getMessage().getPayload().getClass().toString());
		
		
		/* Below condition is used to compare the class type of expected and actual responses */
		
		assertEquals("class org.mule.module.json.JsonData", response.getMessage().getPayload().getClass().toString());
		
	/* Below is code snippet converts the JsonData to HashMap, this convertion is required if indiviual values want to evaluated" */
		
		HashMap data= (HashMap) JSON.parse(response.getMessageAsString());
		
		System.out.println(" the json data is "+data.get("balance"));
		
		assertEquals("1000.2s1", data.get("balance").toString());
		
		
	}
}
