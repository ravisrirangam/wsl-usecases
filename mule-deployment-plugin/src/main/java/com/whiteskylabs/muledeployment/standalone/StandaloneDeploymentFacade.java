package com.whiteskylabs.muledeployment.standalone;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import com.whiteskylabs.muledeployment.util.Constants;
import com.whiteskylabs.muledeployment.util.DeploymentUtil;

public class StandaloneDeploymentFacade {

	static Logger log = Logger.getLogger(StandaloneDeploymentFacade.class
			.getName());

	/**
	 * Move source Mule application zip files to Local Mule Standalone 'apps'
	 * directory.
	 * 
	 * @param muleAppFileName
	 *            Filename of the mule application zip file.
	 * @param srcAbsoluteFilePath
	 *            Absolute file path of the mule application zip file.
	 * @param localStandaloneAppsDir
	 *            Location of the Mule standalone apps directory.
	 * @return
	 * @throws IOException
	 */
	public void copySourceFiletoLocalStandaloneAppsDir(String muleAppFileName,
			String srcMuleAppAbsoluteFilePath, String localStandaloneAppsDir)
			throws IOException {

		log.info("'" + DeploymentUtil.stripExtension(muleAppFileName)
				+ "' copying to apps directory");
		FileInputStream fis = new FileInputStream(srcMuleAppAbsoluteFilePath);
		FileOutputStream fos = new FileOutputStream(localStandaloneAppsDir
				+ muleAppFileName);
		int content;
		while ((content = fis.read()) != -1) {
			fos.write((char) content);
		}
		
		log.info("'" + DeploymentUtil.stripExtension(muleAppFileName)
				+ "' deployed successfully");
		fis.close();
		fos.close();

	}

	/**
	 * Move source Mule application zip files to Remote Mule Standalone 'apps'
	 * directory.
	 * 
	 * @param muleAppFileName
	 *            Filename of the mule application zip file.
	 * @param srcAbsoluteFilePath
	 *            Absolute file path of the mule application zip file.
	 * @param remoteStandaloneAppsDir
	 *            Location of the Mule standalone apps directory.
	 * @return
	 * @throws Exception
	 */
	public void copySourceFiletoRemoteStandaloneAppsDir(String muleAppFileName,
			String srcMuleAppAbsoluteFilePath, String remoteStandaloneAppsDir,
			FTPClient ftpCient) throws Exception {

		// Set the File type 'FTP.BINARY_FILE_TYPE' or else zip will be
		// corrupted.
		ftpCient.setFileType(FTP.BINARY_FILE_TYPE);

		// Place mule application in the remote standalone 'apps' directory
		boolean copiedFileStatus = ftpCient.storeFile(remoteStandaloneAppsDir
				+ muleAppFileName, new FileInputStream(
				srcMuleAppAbsoluteFilePath));

		// If file get copied to 'apps' directory successfully
		if (copiedFileStatus) {
			log.info("'" + muleAppFileName + "' deployed successfully");
			log.debug("'" + muleAppFileName + "' copied to "
					+ remoteStandaloneAppsDir + " directory successfully");

			// if file not get copied to 'apps/ directory
		} else {
			log.error("'"
					+ muleAppFileName
					+ "' deployment failed. Please check FTP server user account permissions");
			log.debug("'"
					+ muleAppFileName
					+ "' not copied to "
					+ remoteStandaloneAppsDir
					+ " directory. Please check FTP server user account permissions");
		}

	}

	/**
	 * Delete mule application in remote standalone apps directory
	 * 
	 * @param deploymentUtil
	 * @param remoteStandaloneAppsDir
	 * @param muleAppName
	 * @return Return true if application get undeployed or else false
	 * @throws Exception
	 */
	public boolean deleteMuleAppInRemoteServer(String remoteStandaloneAppsDir,
			String muleAppName, FTPClient ftpClient) throws Exception {
		try {

			// Strip the extension from the file name.
			String muleAppFilePathWithoutExtension = DeploymentUtil
					.stripExtension(muleAppName);
			boolean isAnchorFileDeleted = false;

			// Get zero down time flag from properties file
			boolean zeroDownTimeFlag = Boolean.parseBoolean(DeploymentUtil
					.getPropertyvalue(Constants.ZERO_DOWN_TIME_FLAG));

			// Create mule application anchor file absolute path
			StringBuffer muleAppAbsoluteFilePath = new StringBuffer(
					remoteStandaloneAppsDir)
					.append(muleAppFilePathWithoutExtension)
					.append(DeploymentUtil
							.getPropertyvalue(Constants.STANDALONE_ANCHOR_FILE_SUFFIX));

			log.info("Undeploying application: "
					+ muleAppFilePathWithoutExtension);
			log.debug("Deleting file: " + muleAppAbsoluteFilePath);

			// if file was not deleted or not existing with some reason
			if (!ftpClient.deleteFile(muleAppAbsoluteFilePath.toString())) {
				log.error("'" + muleAppFilePathWithoutExtension
						+ "' application was not existed in 'apps' directory");
				isAnchorFileDeleted = false;

				// if file was deleted
			} else {
				log.info("'" + muleAppFilePathWithoutExtension
						+ "' undeployed Successfully");
				isAnchorFileDeleted = true;
			}

			// if zero down time is enabled then delete the mule app directory
			// in
			// apps directory
			if (zeroDownTimeFlag) {
				log.info("Zero Down Time is enabled");

				// Create mule app directory path in 'apps' directory
				String tmpMuleAppPath = new StringBuffer()
						.append(remoteStandaloneAppsDir)
						.append(muleAppFilePathWithoutExtension).toString();

				log.info("Deleting '" + muleAppFilePathWithoutExtension
						+ "' directory in " + remoteStandaloneAppsDir
						+ " location");
				// Remove muleApp directory in 'apps' directory
				DeploymentUtil.removeFTPServerDirectory(ftpClient,
						tmpMuleAppPath, "");

			} else {
				log.info("Zero Down Time is disabled");
			}

			return isAnchorFileDeleted;

		} catch (SocketException se) {
			throw new SocketException();
		} catch (IOException ioe) {
			throw new IOException(ioe);
		}
	}

}
