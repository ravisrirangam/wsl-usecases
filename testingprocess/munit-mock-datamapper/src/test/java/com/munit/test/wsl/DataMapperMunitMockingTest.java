package com.munit.test.wsl;

import static junit.framework.Assert.*;

import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

import com.munit.test.commons.DataMapperMockResponse;

/**
 * @author Mohammad rafiq
 *
 */
public class DataMapperMunitMockingTest extends FunctionalMunitSuite {
	
	/*
	 * @return xml file munitMockDataMapper for UnitTesting
	 */
	@Override
	protected String getConfigResources() {
		return "munitMockDataMapper.xml";
	}
	/**
	 * Test-case: 
	 * 1. mock DM component 
	 * 2. then return mock xml response
	 * 3. and assertNotNull
	 **/
	@Test
	public void testDMByMocking() throws Exception {
		
		//getXMLPayload  serve a xml response.
		String xmlPayload=DataMapperMockResponse.getXMLPayload();
		
		//mocking DM component
		whenMessageProcessor("transform").ofNamespace("data-mapper").thenReturn(muleMessageWithPayload(xmlPayload));
		//invoke the flow
		MuleEvent response = runFlow("munitMockDataMapperFlow",testEvent("mock message"));
		//check for response payload NotNull
		assertNotNull(response.getMessageAsString());
	}
}
