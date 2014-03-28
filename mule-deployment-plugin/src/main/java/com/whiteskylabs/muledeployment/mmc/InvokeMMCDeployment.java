package com.whiteskylabs.muledeployment.mmc;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.whiteskylabs.muledeployment.util.DeploymentUtil;

public class InvokeMMCDeployment {

	// Logger object
	public static Logger log = Logger.getLogger(InvokeMMCDeployment.class.getName());
	
	// Flag to deploy on Cluster
	public static boolean isDeploymentTargetCluster;
	
	// Flag to deploy on MMC
	public static boolean isDeploymentTargetMMC;
	
	public static void main(String[] args) throws Exception {
		
		// Configure log4j properties file
		PropertyConfigurator
		.configure("./conf/log4j/mmc/log4j.properties");
		try {
			InvokeMMCDeployment invoke = new InvokeMMCDeployment();
			
			// Call init method
			invoke.init(args[0]);
		} catch (ArrayIndexOutOfBoundsException aie) {
			 log.error(aie.getMessage(), aie);
			 throw new ArrayIndexOutOfBoundsException("Please provide 'propertiesFilePath' parameter with absolute file path of properties file");
		}catch (FileNotFoundException fne) {
			log.error(fne.getMessage(),fne);
			throw new FileNotFoundException();
		}catch (IOException ioe) {
			log.error(ioe.getMessage(),ioe);
			throw new IOException();
		}
	}
	
	/** Initialize the deploement process
	 * @param propertiesFilePath
	 * @throws FileNotFoundException 
	 * @throws Exception
	 */
	public void init(String propertiesFilePath) throws Exception  {
		
		MuleAppDeploymentMMC deploymentImpl = new MuleAppDeploymentMMCImpl();

		// Load properties file
		DeploymentUtil.loadProperties(propertiesFilePath);
		
		// deploy mule applications
		deploymentImpl.deployMuleApp();
		
	}
}