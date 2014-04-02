package com.wsl.munittest;

import org.hornetq.utils.json.JSONException;
import org.hornetq.utils.json.JSONObject;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.munit.runner.functional.FunctionalMunitSuite;
import static junit.framework.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;


public class MockJmsTest  extends FunctionalMunitSuite

{
	@Override
	protected String getConfigResources()
	{
        return "src/main/app/munitjmssample.xml";
    }
	
	
	public List testdata()
	{
		 	List resultOfJdbc = new ArrayList();
	        Map r = new HashMap();
	        r.put("test", "whiteskylabs");
	        resultOfJdbc.add(r);
	        return resultOfJdbc;
			
	}
	
	public Object jsondata() throws JSONException
	{
		
		  JSONObject obj = new JSONObject();

	      obj.put("name", "foo");
	      obj.put("num", new Integer(100));
	      obj.put("balance", new Double(1000.21));
	      obj.put("is_vip", new Boolean(true));

	     
		
		return obj;
		
	}
	
	
	public void Mockjmsoutbound() throws Exception
	{
		whenEndpointWithAddress("jms://ExampleQueue").thenReturn(muleMessageWithPayload(jsondata()));
		
		
	}
	
//	@Test
	public void MockAppendStringprocessor() throws Exception
	{
		whenEndpointWithAddress("jms://ExampleQueue").thenReturn(muleMessageWithPayload(testdata()));
		
		whenMessageProcessor("append-string-transformer").thenReturn(muleMessageWithPayload("mock append"));
		
		MuleEvent response=runFlow("munitjmssampleFlow", testEvent("testing from Junit"));
		
		assertEquals("iam able to mock jms endpoint", response.getMessageAsString());
	}
	
	
	//@Test
	public void MockDataMapper() throws Exception
	{
		whenEndpointWithAddress("jms://ExampleQueue").thenReturn(muleMessageWithPayload(testdata()));
		
		whenMessageProcessor("append-string-transformer").thenReturn(muleMessageWithPayload("mock append"));
		
		whenMessageProcessor("transform").ofNamespace("data-mapper").thenReturn(muleMessageWithPayload("mock datamapperdata"));
		
		MuleEvent response=runFlow("munitjmssampleFlow", testEvent("testing from Junit"));
		
		assertEquals("iam able to mock jms endpoint", response.getMessageAsString());
	}
	
}
