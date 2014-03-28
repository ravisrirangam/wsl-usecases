package com.whiteskylabs.muledeployment.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.whiteskylabs.muledeployment.standalone.InvokeStanaloneDeployment;

public class Configuration {

	static Logger log = Logger.getLogger(InvokeStanaloneDeployment.class
			.getName());

	/** Get FTP Connection 
	 * @param userName Username of the FTP server
	 * @param password Password of the FTP server
	 * @param hostname Hostname of the FTP server
	 * @param port Port number of the FTP server
	 * @return Return FTPClient object
	 * @throws Exception
	 */
	public static FTPClient getFTPClient(String userName, String password,
			String hostname, int port) throws Exception {

		FTPClient ftp = new FTPClient();
		
		// Connect to the server system
		ftp.connect(hostname, port);
		int reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("Exception in connecting to FTP Server");
        }
		log.debug("Reply code is:" + reply);

		// Login to FTP server.
		if(ftp.login(userName, password)){
			log.info("logged into FTP server");
		}else{
			throw new Exception("FTP Server Login Failed");
			
		}

		return ftp;
	}
}
