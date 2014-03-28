package com.whiteskylabs.muledeployment.mmc;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface MuleAppDeploymentMMC {

	/** Deploy the Mule application on Mule Management Console(MMC)
	 * @param deploymentUtil object of DeploymentUtil
	 * @throws Exception
	 */
	public void deployMuleApp() throws Exception ;
	
	/** Undeploy the mule application form MMC server
	 * @param muleAppName Mule application name
	 * @param deploymentUtil object of DeploymentUtil
	 * @throws Exception
	 */
	public void undeployMuleApp(String muleAppName) throws Exception ;
	
	/** Clear the cache of undeployed mule application (deletes mule app in 'tmp' directory)
	 * @param deploymentUtil object of DeploymentUtil
	 * @param muleAppName Mule application name
	 * @throws FileNotFoundException 
	 * @throws IOException
	 */
	public void clearMuleAppCache(String muleAppName) throws FileNotFoundException, IOException;
	
	/** Delete the log files of undeployed mule application
	 * @param deploymentUtil object of DeploymentUtil
	 * @param muleAppName Mule application name
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void deleteMuleAppLogs(String muleAppName) throws FileNotFoundException, IOException;
	
}
