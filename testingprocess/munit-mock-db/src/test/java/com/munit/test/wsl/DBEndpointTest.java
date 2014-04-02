package com.munit.test.wsl;

import static junit.framework.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.munit.runner.functional.FunctionalMunitSuite;

/**
 * @author Mohammad rafiq
 *
 */
public class DBEndpointTest extends FunctionalMunitSuite {

	/**
	 *  
	 * @return The location of your MULE flow file.
	 */

	@Override
	protected String getConfigResources() {
		return "munitMockDB.xml";
	}

	/**
	 * @return a collection of map object as getJDBCPayload
	 */
	public List jdbcPayload() {
		HashMap<String,String> mapObj1= new HashMap<String, String>();
		HashMap<String,String> mapObj2= new HashMap<String, String>();
		
		mapObj1.put("COLUMN1", "1");
		mapObj1.put("COLUMN2", "row1");
		mapObj2.put("COLUMN1", "2");
		mapObj2.put("COLUMN2", "row2");
		
		List list = new ArrayList<String>();
		
		list.add(mapObj1);
		list.add(mapObj2);
		
		return list;
	}

	/**
	 * TestCase: should mock DB outbound-endpoint, 
	 * When mocked it should return payload from method jdbcPayload() 
	 * i.e [{COLUMN2=row1, COLUMN1=1}, {COLUMN2=row2, COLUMN1=2}]
	 */
	@Test
	public void testforDBMock() throws Exception {
		whenEndpointWithAddress("jdbc://selectQ").thenReturn( muleMessageWithPayload( jdbcPayload() ) );
		MuleEvent resultEvent = runFlow("munit-db-mockFlow",testEvent("Hello world!"));
		System.out.println(jdbcPayload().toString());
		assertNotNull(resultEvent.getMessage().getPayloadAsString());
	}
}