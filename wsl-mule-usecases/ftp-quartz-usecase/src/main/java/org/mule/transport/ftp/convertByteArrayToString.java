package org.mule.transport.ftp;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;



public class convertByteArrayToString  extends AbstractMessageTransformer
{
   @Override
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		// TODO Auto-generated method stub
	   System.out.println("************ Inside transformer");
		Object obj = message.getPayload();
		//System.out.println("===== "+obj.getClass());
		List<byte[]> files = (List<byte[]>) obj;
		int size = files.size();
   	for(int i = 0; i < size; i++) {
   		byte[] f = files.get(i);
   		try {
				processFile(f);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
   		
   	}
   	return "Test";
	}	
	void processFile(byte[] b) throws Exception {
		String s = null;
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(b)));
		while((s = br.readLine()) !=null) {
			sb.append(s).append("\n");
		}
		
		System.out.println(sb.toString());
	}
}

