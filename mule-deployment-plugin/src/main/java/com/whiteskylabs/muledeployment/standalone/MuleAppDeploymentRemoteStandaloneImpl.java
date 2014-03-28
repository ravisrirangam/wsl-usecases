package com.whiteskylabs.muledeployment.standalone;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

import com.whiteskylabs.muledeployment.util.Constants;
import com.whiteskylabs.muledeployment.util.DeploymentUtil;

public class MuleAppDeploymentRemoteStandaloneImpl implements
		MuleAppDeploymentStandalone {

	static Logger log = Logger
			.getLogger(MuleAppDeploymentLocalStandaloneImpl.class.getName());

	FTPClient ftpClient;

	@Override
	public void deployMuleApp() throws Exception {

		// get Mule Remote standalone 'apps' directory from properties file
		String remoteStandaloneAppsDir = DeploymentUtil
				.getPropertyvalue(Constants.REMOTE_STANDALONE_APPS_DIR);

		// get source mule applications directory from properties file
		String sourceAppsDir = DeploymentUtil
				.getPropertyvalue(Constants.STANDALONE_MULE_APPS_SRC_DIR);

		// Get FTPClient connection object
		ftpClient = DeploymentUtil.getFTPClient();

		// get List of mule application names
		String[] listOfMuleApps = DeploymentUtil.validateAndGetListOfFileNames(
				sourceAppsDir, Constants.MULE_APP_FILE_EXT);

		// Iterate all mule applications and deploy each one after another
		for (String muleAppFileName : listOfMuleApps) {

			// Create a source mule Application absolute file path
			String srcMuleAppAbsoluteFilePath = new StringBuffer(sourceAppsDir)
					.append(muleAppFileName).toString();

			// Undeploy the existing mule application
			StandaloneDeploymentFacade facade = new StandaloneDeploymentFacade();

			// If any deployment/application get undeployed then clear Cache
			// before deploy
			// or else deploy the mule application directly
			if (UndeployExistingMuleApp(remoteStandaloneAppsDir,
					muleAppFileName)) {

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
									.getPropertyvalue(Constants.REMOTE_STANDALONE_TMP_DIR));
				}

				// Delete the undeployed mule application log files
				if (deleteLogFilesFlag) {

					// Delete the existing mule application log files
					deleteMuleAppLogFiles(
							muleAppFileName,
							DeploymentUtil
									.getPropertyvalue(Constants.REMOTE_STANDALONE_LOGS_DIR));
				}
			}

			// Deploy the mule application
			facade.copySourceFiletoRemoteStandaloneAppsDir(muleAppFileName,
					srcMuleAppAbsoluteFilePath, remoteStandaloneAppsDir,
					ftpClient);
			log.info("---------------------------------------");
		}
		// If FTP connection still alive logout and disconnect
		if (ftpClient.isConnected()) {
			ftpClient.logout();
			ftpClient.disconnect();
		}
		

	}

	@Override
	public boolean UndeployExistingMuleApp(String muleStandaloneAppsDir,
			String muleAppName) throws Exception {
		StandaloneDeploymentFacade facade = new StandaloneDeploymentFacade();

		// Delete mule application anchor files in standalone 'apps' directory.
		return facade.deleteMuleAppInRemoteServer(muleStandaloneAppsDir,
				muleAppName, ftpClient);

	}

	@Override
	public void clearMuleAppCache(String muleAppName, String muleServerTmpDir)
			throws Exception {

		// Strip the extension(.zip) from file name.
		muleAppName = DeploymentUtil.stripExtension(muleAppName);

		// Create mule app directory path
		String tmpMuleAppPath = new StringBuffer().append(muleServerTmpDir)
				.append(muleAppName).toString();

		// Remove muleApp directory in 'tmp' directory
		DeploymentUtil.removeFTPServerDirectory(ftpClient, tmpMuleAppPath, "");

	}

	@Override
	public void deleteMuleAppLogFiles(String muleAppName,
			String muleServerLogsDir) throws FileNotFoundException, IOException {

		// Get Log file prefix pattern from properties file
		String logFilePrefix = DeploymentUtil
				.getPropertyvalue(Constants.MULE_APP_LOG_FILE_PREFIX);

		// Strip the extension(.zip) from file name.
		muleAppName = DeploymentUtil.stripExtension(muleAppName);

		// Create mule app log file name pattern to get list of files
		String muleAppLogFilePattern = new StringBuffer(logFilePrefix).append(
				muleAppName).toString();

		// To set log file name
		String logFileName;

		// To set log file path
		String logFilePath;

		// Loop to get log files based on pattern and delete those log files
		for (FTPFile logFile : ftpClient.listFiles(muleServerLogsDir)) {

			// Process log file if matches the file name pattern
			if (logFile.getName().startsWith(muleAppLogFilePattern)) {

				// Get log file name
				logFileName = logFile.getName();

				// Set Log file path
				logFilePath = new StringBuffer(muleServerLogsDir).append(
						logFileName).toString();
				log.debug("Deleting Log file name: " + logFileName);

				// Delete log file and display the status
				log.debug("Delete file status: "
						+ ftpClient.deleteFile(logFilePath));
			}

		}

	}

}
