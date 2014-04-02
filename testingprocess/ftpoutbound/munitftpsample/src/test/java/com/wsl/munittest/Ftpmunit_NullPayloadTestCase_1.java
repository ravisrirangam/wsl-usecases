package com.wsl.munittest;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

public class Ftpmunit_NullPayloadTestCase_1 extends FunctionalMunitSuite

{
	@Override
	protected String getConfigResources()
	{
        return "src/main/app/munitftpsample.xml";
    }
	
	@Test
	
	public void ftpunittest() throws Exception
	{
		String data="";
		MuleEvent response=runFlow("munitftpsampleFlow", testEvent(data));
		
		System.out.println(" response from mule flow is "+response.getMessageAsString());
		
		assertNotSame("", response.getMessageAsString());
	}
}
