package com.whiteskylabs.muledeployment.mmc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.whiteskylabs.muledeployment.util.Constants;
import com.whiteskylabs.muledeployment.util.DeploymentUtil;

public class MuleAppDeploymentMMCImpl implements MuleAppDeploymentMMC {

	public static Logger log = Logger.getLogger(MuleAppDeploymentMMCImpl.class
			.getName());

	@Override
	public void deployMuleApp() throws Exception {

		MMCDeploymentFacade facade = new MMCDeploymentFacade();

		// get source mule applications directory from properties file
		String sourceAppsDir = DeploymentUtil
				.getPropertyvalue(Constants.MMC_MULE_APPS_SRC_DIR);

		// Validate the give directory location and get the List of mule
		// application names
		String[] list = DeploymentUtil.validateAndGetListOfFileNames(
				sourceAppsDir, Constants.MULE_APP_FILE_EXT);

		// Deploy all available zip files in mule server.
		for (String file : list) {
			String temp = new StringBuffer(sourceAppsDir)
					.append(File.separator).append(file).toString();
			String muleAppName = DeploymentUtil.stripExtension(file);

			// Undeploy the Application with same name before deploy the new
			// one.
			undeployMuleApp(muleAppName);

			// Deploy the applicaiton
			facade.deploy(temp, muleAppName);
		}
	}

	@Override
	public void undeployMuleApp(String muleAppName) throws Exception {
		MMCDeploymentFacade facade = new MMCDeploymentFacade();

		// Set REST API URL according to target server (MMC or Cluster) 
		String getListAllURL;
		
		// If target server is MMC
		if (InvokeMMCDeployment.isDeploymentTargetMMC) {
			getListAllURL = "http://"
					+ DeploymentUtil.getPropertyvalue(Constants.MMC_HOST) + ":"
					+ DeploymentUtil.getPropertyvalue(Constants.MMC_PORT)
					+ "/mmc/api/deployments?server=" + DeploymentUtil.getPropertyvalue(Constants.MMC_SERVER_ID);
		}
		
		// If target server is Cluster
		else if (InvokeMMCDeployment.isDeploymentTargetCluster) {
			getListAllURL = "http://"
					+ DeploymentUtil.getPropertyvalue(Constants.MMC_HOST) + ":" 
					+ DeploymentUtil.getPropertyvalue(Constants.MMC_PORT)
					+ "/mmc/api/deployments?cluster=" + DeploymentUtil.getPropertyvalue(Constants.MMC_CLUSTER_ID);
		}

		// If target server is not provided
		else {
			throw new Exception("Target server(MMC or Cluster) is not provided");
		}
		
		// Get all deployments data
		String listAllMuleAppsData = facade.getListAllMuleAppsData(getListAllURL);
		
		// Get specified mule app href.
		String muleApphref = facade.getMuleAppRef(muleAppName,
				listAllMuleAppsData);

		/*
		 * If specified mule application is available in deployments then
		 * undeploy and delete the deployment
		 */
		if (muleApphref != null) {
			log.info("Deployment is already created in the name of'"
					+ muleAppName
					+ "'. Lets undeploy and delete the deployment");

			// Undeploy the application
			facade.undeploy(muleApphref, muleAppName);

			// Delete the deployment
			facade.deleteDeployment(muleApphref, muleAppName);

			// Get clearCacheStatus Flag from properties file
			boolean clearCacheStatusFlag = Boolean.parseBoolean(DeploymentUtil
					.getPropertyvalue(Constants.MMC_CLEAR_CACHE_FLAG));

			// Get deleteLogFilesFlag from properties file
			boolean deleteLogFilesFlag = Boolean.parseBoolean(DeploymentUtil
					.getPropertyvalue(Constants.MMC_DELETE_LOGS_FILES_FLAG));
			
			// if clearCacheStatusFlag is true then delete mule app details in
			// tmp directory
			if (clearCacheStatusFlag) {

				// Delete the specified mule application file in 'tmp' folder
				clearMuleAppCache(muleAppName);
			}
			
			// deleteLogFilesFlag is enabled delete the log files 
			if(deleteLogFilesFlag){
			
				// Delete the specified mule application log files in 'logs'
				// folder
				deleteMuleAppLogs(muleAppName);
			}
		} else {
			log.info("Source mule applications are not existed in the deployments to undeploy");
		}
	}

	@Override
	public void clearMuleAppCache(String muleAppName)
			throws FileNotFoundException, IOException {
		String muleServerTmpDir = DeploymentUtil
				.getPropertyvalue(Constants.MMC_TMP_DIR);

		// Clear specified application cache
		DeploymentUtil.clearMuleAppCache(muleAppName, muleServerTmpDir);
	}

	@Override
	public void deleteMuleAppLogs(String muleAppName)
			throws FileNotFoundException, IOException {

		String muleServerLogsDir = DeploymentUtil
				.getPropertyvalue(Constants.MMC_LOGS_DIR);
		String logFilePrefix = DeploymentUtil
				.getPropertyvalue(Constants.MULE_APP_LOG_FILE_PREFIX);

		// Clear specified application log files
		DeploymentUtil.deleteMuleAppLogFiles(muleAppName, muleServerLogsDir,
				logFilePrefix);
	}
}
