package com.whiteskylabs.munit.aggregators;

import static junit.framework.Assert.assertNotNull;

import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

public class CollectionAggregatorTest extends FunctionalMunitSuite {

	
	@Test
	public void testNotNullHTTPMunit() throws Exception
	{
		
//		mockEndpoint(address, mockPayload);
		
		MuleEvent responseEvent = runFlow("collection-aggregatorFlow3", testEvent("test HTTP Payload"));
		
		assertNotNull(responseEvent.getMessageAsString());
		System.out.println(responseEvent.getMessageAsString());
	}
}
