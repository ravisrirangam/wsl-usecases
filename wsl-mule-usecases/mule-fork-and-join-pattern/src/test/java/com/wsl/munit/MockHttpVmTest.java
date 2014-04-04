package com.wsl.munit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

public class MockHttpVmTest  extends FunctionalMunitSuite {

	@Override
	protected String getConfigResources() {
		// TODO Auto-generated method stub
		return "src/main/app/testvmhttp.xml";
	}
	
	
	@Test
	public void testvmhttpFlow() throws Exception {
		/*Integer cost = new java.lang.Double(1000.0 * Math.random()).intValue();
		whenEndpointWithAddress("http://localhost:8081/atStorePrice").thenReturn(muleMessageWithPayload(cost));
		whenEndpointWithAddress("http://localhost:8081/atMarketPrice").thenReturn(muleMessageWithPayload(cost));
		Collection c=new LinkedList() ;
		c.add(cost);
		c.add(cost);
		whenMessageProcessor("request-reply").thenReturn(muleMessageWithPayload(c));
		
		//whenMessageProcessor("expression-transformer").thenReturn(muleMessageWithPayload(cost));
		System.out.println(cost + "##--------------888----------->");*/
		
		System.out.println("##-----------$$$$$$$$$$$$$$$$$$----------->");
		MuleEvent resultevnt = runFlow("testvmhttpFlow1", testEvent(""));
		
		System.out.println("##-----------^^^^^^^^^^^^^^^^^^^---------->");
		assertNotNull(resultevnt.getMessage().getPayload());

		System.out.println("##------------------------->"+ resultevnt.getMessage().getPayload());

		assertEquals("mho", resultevnt.getMessage().getPayload().toString());
		assertEquals("class java.lang.String", resultevnt.getMessage().getPayload().getClass().toString());
		assertNotSame("class org.mule.module.json.JsonData", resultevnt.getMessage().getPayload().getClass().toString());
	}
	
}
