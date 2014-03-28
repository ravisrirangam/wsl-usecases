package com.whiteskylabs.muledeployment.standalone;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.whiteskylabs.muledeployment.util.Constants;
import com.whiteskylabs.muledeployment.util.DeploymentUtil;

public class MuleAppDeploymentLocalStandaloneImpl implements
		MuleAppDeploymentStandalone {

	static Logger log = Logger
			.getLogger(MuleAppDeploymentLocalStandaloneImpl.class.getName());

	@Override
	public void deployMuleApp() throws Exception {

		// get Mule standalone home directory from properties file
		String muleStandaloneAppsDir = DeploymentUtil
				.getPropertyvalue(Constants.STANDALONE_APPS_DIR);

		// get source mule applications directory from properties file
		String sourceAppsDir = DeploymentUtil
				.getPropertyvalue(Constants.STANDALONE_MULE_APPS_SRC_DIR);
		StandaloneDeploymentFacade facade = new StandaloneDeploymentFacade();

		// get List of mule application names
		String[] list = DeploymentUtil.validateAndGetListOfFileNames(
				sourceAppsDir, Constants.MULE_APP_FILE_EXT);


		for (String muleAppFileName : list) {

			String absoluteFilePath = new StringBuffer(sourceAppsDir)
					.append(File.separator).append(muleAppFileName).toString();

			// Undeploy the existing mule application and if application
			// existing and undeployed successfully then clear cache(logs and
			// tmp files of application)
			if (UndeployExistingMuleApp(muleStandaloneAppsDir, muleAppFileName)) {

				// Get clearCacheStatus Flag from properties file
				boolean clearCacheStatusFlag = Boolean
						.parseBoolean(DeploymentUtil
								.getPropertyvalue(Constants.STANDALONE_CLEAR_CACHE_FLAG));

				// Get deleteLogFilesFlag from properties file
				boolean deleteLogFilesFlag = Boolean
						.parseBoolean(DeploymentUtil
								.getPropertyvalue(Constants.STANDALONE_DELETE_LOGS_FILE_FLAG));
				
				// if clearCacheStatusFlag is true then delete mule app details
				// in tmp and logs directory
				if (clearCacheStatusFlag) {

					// Clear the mule application cache in tmp directory
					clearMuleAppCache(
							muleAppFileName,
							DeploymentUtil
									.getPropertyvalue(Constants.STANDALONE_TMP_DIR));
				}
				
				// Delete the undeployed mule application log files
				if(deleteLogFilesFlag){
					
					deleteMuleAppLogFiles(
							muleAppFileName,
							DeploymentUtil
									.getPropertyvalue(Constants.STANDALONE_LOGS_DIR));
				}
			}

			// Deploy the mule application
			facade.copySourceFiletoLocalStandaloneAppsDir(muleAppFileName,
					absoluteFilePath, muleStandaloneAppsDir);
			log.info("---------------------------------------");
		}

	}

	@Override
	public boolean UndeployExistingMuleApp(String muleStandaloneAppsDir,
			String muleAppName) throws FileNotFoundException, IOException {

		// strip the extension from file name.
		String muleAppFileNameWithOutExtension = DeploymentUtil
				.stripExtension(muleAppName);

		boolean isAnchorFileDeleted = false;
		boolean isMuleAppDirDeleted = false;

		// Get zero down time flag from properties file
		boolean zeroDownTimeFlag = Boolean.parseBoolean(DeploymentUtil
				.getPropertyvalue(Constants.ZERO_DOWN_TIME_FLAG));

		/*
		 * Prepare the mule application anchor file
		 * 
		 * Every Deployment has an anchor file in the apps directory. Delete the
		 * anchor file to undeploy the application in a clean way.
		 */
		StringBuffer absoluteFileName = new StringBuffer(muleStandaloneAppsDir)
				.append(muleAppFileNameWithOutExtension)
				.append(DeploymentUtil
						.getPropertyvalue(Constants.STANDALONE_ANCHOR_FILE_SUFFIX));

		File targetFile = new File(absoluteFileName.toString());

		// if file exists
		if (targetFile.isFile()) {

			// if file is not deleted with some reason
			if (!targetFile.delete()) {
				log.error(muleStandaloneAppsDir + muleAppName
						+ " was not undeployed");
				isAnchorFileDeleted = false;

				// if file is deleted
			} else {
				log.info("'" + muleAppFileNameWithOutExtension
						+ "' undeployed Successfully");
				isAnchorFileDeleted = true;
			}
		}

		// if file is not existed in apps directory
		else {
			log.error("'" + absoluteFileName
					+ "' is not existing in 'apps' directory ");
			isAnchorFileDeleted = false;
		}

		// if zero down time is enabled then delete the mule app directory in
		// apps directory
		if (zeroDownTimeFlag) {

			// Set mule app directory path
			File muleAppDir = new File(muleStandaloneAppsDir
					+ muleAppFileNameWithOutExtension);

			// If it is a directory delete it
			if (muleAppDir.isDirectory()) {

				log.info("Deleting '" + muleAppFileNameWithOutExtension
						+ "' directory in " + muleStandaloneAppsDir
						+ " location");

				// Delete mule app directory
				DeploymentUtil.deleteLocalDir(muleAppDir);
				isMuleAppDirDeleted = true;
				log.debug("'" + muleStandaloneAppsDir
						+ muleAppFileNameWithOutExtension
						+ "' directory was deleted");
			} else {
				log.error("'" + muleStandaloneAppsDir
						+ muleAppFileNameWithOutExtension
						+ "' directory is not existing in 'apps' directory");
			}

			// If anchor and mule app directory get deleted successfully then
			// return true
			if (isAnchorFileDeleted && isMuleAppDirDeleted) {
				log.debug("'" + muleAppDir + "' and '" + targetFile
						+ "'  are deleted successfully");
				return true;

				// else any one of them is not deleted then return false
			} else {
				return false;
			}
		}

		// else zero down time is not enable then return the anchor file
		// deletion status.
		else {
			return isAnchorFileDeleted;
		}

	}

	@Override
	public void clearMuleAppCache(String muleAppName, String muleServerTmpDir)
			throws FileNotFoundException, IOException {

		DeploymentUtil.clearMuleAppCache(muleAppName, muleServerTmpDir);

	}

	@Override
	public void deleteMuleAppLogFiles(String muleAppName,
			String muleServerLogsDir) throws FileNotFoundException, IOException {

		String logFilePrefix = DeploymentUtil
				.getPropertyvalue(Constants.MULE_APP_LOG_FILE_PREFIX);
		DeploymentUtil.deleteMuleAppLogFiles(muleAppName, muleServerLogsDir,
				logFilePrefix);

	}

}
