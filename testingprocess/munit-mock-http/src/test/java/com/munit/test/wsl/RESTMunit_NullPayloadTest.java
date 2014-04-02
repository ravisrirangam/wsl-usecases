package com.munit.test.wsl;

import static junit.framework.Assert.assertNotNull;

import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

import com.munit.test.common.RequestMessageBuilder;


public class RESTMunit_NullPayloadTest extends FunctionalMunitSuite {
	/**
	 * 
	 * @return The location of your MULE flow file.
	 */
	@Override
	protected String getConfigResources() {
		return "munitMockHTTP-REST.xml";
	}

	/**
	 * TestCase: should mock HTTP outbound-endpoint with the address below
	 * http://localhost:8686/ActualRestServer/RestServerClass?param1=Team, When
	 * mocked it should return payload as 'mock message'
	 */
	@Test
	public void mockHTTP_REST_NotNull() throws Exception {

		//whenEndpointWithAddress("http://localhost:8686/ActualRestServer/RestServerClass?param1=Team").thenReturn(muleMessageWithPayload("mock message as Hai Team"));
		MuleEvent resultEvent = runFlow("munit-http-RestConsumer",testEvent(new RequestMessageBuilder().restInputMessageForGETOperation(muleContext)));
		System.out.println(resultEvent.getMessage().getPayload().getClass().toString());
		assertNotNull(resultEvent.getMessage().getPayload());
	}

}