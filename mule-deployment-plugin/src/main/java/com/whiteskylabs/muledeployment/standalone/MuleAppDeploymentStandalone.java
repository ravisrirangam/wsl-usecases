package com.whiteskylabs.muledeployment.standalone;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface MuleAppDeploymentStandalone {

	/** Deploy the Mule application on Mule standalone
	 * @param deploymentUtil object of DeploymentUtil 
	 * @throws Exception
	 */
	public void deployMuleApp() throws Exception;

	/** Delete the existing Mule application on Stand alone server
	 * @param muleStandaloneAppsDir Location of Mule Standalone apps directory 
	 * @param targetFileName File name of the existing mule application
	 * @return return true if application exists undeployed successfully or else false
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public boolean UndeployExistingMuleApp(String muleStandaloneAppsDir,
			String muleAppName) throws Exception;

	/** Delete undeployed Mule application cache in Mule standalone tmp directory
	 * @param muleAppName Mule application name
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void clearMuleAppCache(String muleAppName, String muleServerTmpDir) throws Exception ;

	/** Delete undeployed application log files
	 * @param muleAppName Mule application name
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void deleteMuleAppLogFiles(String muleAppName, String muleServerLogsDir) throws FileNotFoundException, IOException ;

}
