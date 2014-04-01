package com.wsl.munittest;

import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.munit.config.AssertNotNullMessageProcessor;
import org.mule.munit.runner.functional.FunctionalMunitSuite;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;

public class JmsMunit_NullPayloadTestCase_1  extends FunctionalMunitSuite
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
		MuleEvent response=runFlow("munitjmssampleFlow", testEvent(""));
		
		System.out.println(" response from mule flow is "+response.getMessageAsString());
		
		/* Below condition checks whether response from mule flow is null or not */
		assertNotNull(response.getMessageAsString());
		
	}
}
