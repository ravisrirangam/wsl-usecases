import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.client.MuleClient;
import org.mule.munit.runner.functional.FunctionalMunitSuite;
import org.junit.experimental.categories.Category;
import com.wsl.munittest.MockFtp;
import de.schlichtherle.io.FileWriter;

import org.mule.munit.*;

@Category(IntegrationTests.class)
public class FIleTest extends FunctionalMunitSuite

{
	@Override
	protected String getConfigResources()
	{
        return "src/main/app/munitftpsample.xml";
    }
	
	@Test	
	public void fileintegrationtest() throws Exception
	{
		createfile();
		
		MuleClient client = muleContext.getClient();
		
		MuleEvent response=runFlow("munitftpsampleFlow",testEvent("ff"));		
		
		System.out.println(" response from mule flow is "+response.getMessage().getPayload());
		
		assertNotNull(response.getMessageAsString());
		
		assertEquals("white sky labs", response.getMessageAsString());
		
	}
	
	public MuleEvent createfile() throws IOException
	{
		String testdata="testing from file";
		
		String filePath="F:/vikramwork/file/sample.txt";
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
		
		bw.write(testdata);
		
		bw.close();
		return null;
	}
	
}
