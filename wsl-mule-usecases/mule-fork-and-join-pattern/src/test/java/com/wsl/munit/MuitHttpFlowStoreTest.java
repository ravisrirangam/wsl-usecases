package com.wsl.munit;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

public class MuitHttpFlowStoreTest extends FunctionalMunitSuite {

	@Override
	protected String getConfigResources() {
		// TODO Auto-generated method stub
		return "src/main/app/mule-fork-and-join-pattern.xml";
	}

	@Test
	public void teststore1Flow() throws Exception {
		Integer cost = new java.lang.Double(1000.0 * Math.random()).intValue();
		// whenEndpointWithAddress("http://localhost:8081/atStorePrice").thenReturn(muleMessageWithPayload(cost));

		whenMessageProcessor("expression-transformer").thenReturn(
				muleMessageWithPayload(cost));

		MuleEvent resultevnt = runFlow("store1Flow", testEvent(cost));
		assertNotNull(resultevnt.getMessage().getPayload());

		System.out.println(cost + "##------------------------->"
				+ resultevnt.getMessage().getPayload().getClass());

		assertEquals(cost, resultevnt.getMessage().getPayload());
		assertEquals("class java.lang.Integer", resultevnt.getMessage()
				.getPayload().getClass().toString());
		assertNotSame("class org.mule.module.json.JsonData", resultevnt
				.getMessage().getPayload().getClass().toString());
	}

	@Test
	public void testMarket1Flow() throws Exception {
		Integer cost = new java.lang.Double(1000.0 * Math.random()).intValue();
		// whenEndpointWithAddress("http://localhost:8081/atStorePrice").thenReturn(muleMessageWithPayload(cost));

		whenMessageProcessor("expression-transformer").thenReturn(
				muleMessageWithPayload(cost));

		MuleEvent resultevnt = runFlow("store2Flow", testEvent(cost));
		assertNotNull(resultevnt.getMessage().getPayload());

		System.out.println(cost + "##------------------------->"
				+ resultevnt.getMessage().getPayload().getClass());

		assertEquals(cost, resultevnt.getMessage().getPayload());
		assertEquals("class java.lang.Integer", resultevnt.getMessage()
				.getPayload().getClass().toString());
		assertNotSame("class org.mule.module.json.JsonData", resultevnt
				.getMessage().getPayload().getClass().toString());
	}
}
