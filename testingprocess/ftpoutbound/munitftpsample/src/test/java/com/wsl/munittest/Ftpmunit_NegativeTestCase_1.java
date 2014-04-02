package com.wsl.munittest;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

public class Ftpmunit_NegativeTestCase_1 extends FunctionalMunitSuite

{
	@Override
	protected String getConfigResources()
	{
        return "src/main/app/munitftpsample.xml";
    }
	
	@Test
	
	public void ftpunittest() throws Exception
	{
		MuleEvent response=runFlow("munitftpsampleFlow", testEvent("white sky labs"));
		
		System.out.println(" response from mule flow is "+response.getMessage().getPayload());
		
		assertNotNull(response.getMessageAsString());
		
		assertNotSame("sky labs", response.getMessageAsString());
	}
}
