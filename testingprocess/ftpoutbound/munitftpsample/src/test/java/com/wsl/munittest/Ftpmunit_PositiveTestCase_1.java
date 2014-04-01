package com.wsl.munittest;

import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.module.json.JsonData;
import org.mule.munit.config.AssertNotNullMessageProcessor;
import org.mule.munit.runner.functional.FunctionalMunitSuite;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class Ftpmunit_PositiveTestCase_1 extends FunctionalMunitSuite
{
	@Override
	protected String getConfigResources()
	{
        return "src/main/app/munitftpsample.xml";
    }
	
	@Test
	
	public void ftpunittest() throws Exception
	{
		
		MockFtp mf= new MockFtp();
		
		mf.ftpmock();
		
		MuleEvent response=runFlow("munitftpsampleFlow", testEvent("white sky labs"));
		
		System.out.println(" response from mule flow is "+response.getMessage().getPayload());
		
		assertNotNull(response.getMessageAsString());
		
		assertEquals("white sky labs", response.getMessageAsString());
	}
}
