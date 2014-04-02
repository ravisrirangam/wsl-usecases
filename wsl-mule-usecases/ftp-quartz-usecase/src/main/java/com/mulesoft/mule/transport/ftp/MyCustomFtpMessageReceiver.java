package com.mulesoft.mule.transport.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.mule.api.MuleMessage;
import org.mule.api.construct.FlowConstruct;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.CreateException;
import org.mule.api.transport.Connector;
import org.mule.transport.ftp.FtpConnector;
import org.mule.transport.ftp.FtpMuleMessageFactory;
import org.mule.util.IOUtils;

public class MyCustomFtpMessageReceiver extends EEFtpMessageReceiver {
	public final FtpConnector connector;

	public MyCustomFtpMessageReceiver(Connector connector,
			FlowConstruct service, InboundEndpoint endpoint, long frequency,
			String moveToDir, String moveToPattern, long fileAge)
			throws CreateException {
		super(connector, service, endpoint, frequency, moveToDir,
				moveToPattern, fileAge);
		// TODO Auto-generated constructor stub
		this.connector = ((FtpConnector) connector);
	}

	@Override
	public void poll() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Inside the class in polling method");
		FTPFile[] files = listFiles();
		System.out.println("No of files received" + files.length );
		List<byte[]> contents = extarctFiles(files);
		//System.out.println("File Content:" + contents);		
		MuleMessage msg = createMuleMessage(contents,"UTF-8");
		this.routeMessage(msg);
 
	}

	private List<byte[]> extarctFiles(FTPFile[] files) {
		List<byte[]> ret = new ArrayList<byte[]>();
		System.out.println("Inside extractFiles method");
		try {
			FTPClient client = connector.getFtp(endpoint.getEndpointURI());
			System.out.println("Inside extractFiles method");
			for (int i = 0; i < files.length; i++) {
				System.out.println("Inside for loop");
				FTPFile file = files[i];
				InputStream is = client.retrieveFileStream(file.getName());
				System.out.println("Input stream is:" + is);
				byte[] b = IOUtils.toByteArray(is);
				ret.add(b);			

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public void processFile(FTPFile[] file) throws Exception {
		System.out.println("Inside the class in process method");
		FTPClient client = null;
		client = connector.getFtp(endpoint.getEndpointURI());
		System.out.println("client created");

		FtpMuleMessageFactory muleMessageFactory = createMuleMessageFactory(client);
		MuleMessage message;
		for (int i = 0; i < file.length; i++) {
			System.out.println("inside of for loop");
			System.out.println(file[i]);
			message = muleMessageFactory.create(file, endpoint.getEncoding());
			System.out.println(message);
			// muleMessageFactory.create(transportMessage, encoding)
			System.out.println("message created");
			routeMessage(message);
			System.out.println("message routed");
			postProcess(client, file[i], message);
		}

	}

}