package com.whiteskylabs.muledeployment.standalone;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.whiteskylabs.muledeployment.util.Constants;
import com.whiteskylabs.muledeployment.util.DeploymentUtil;

public class InvokeStanaloneDeployment {

	static Logger log = Logger.getLogger(InvokeStanaloneDeployment.class
			.getName());

	public static void main(String[] args) throws Exception {
		PropertyConfigurator
				.configure("./conf/log4j/standalone/log4j.properties");
		try {
			InvokeStanaloneDeployment invoke = new InvokeStanaloneDeployment();
			invoke.init(args[0]);
		} catch (ArrayIndexOutOfBoundsException aie) {
			log.error(aie.getMessage(), aie);
			throw new ArrayIndexOutOfBoundsException(
					"Please provide 'propertiesFilePath' parameter with absolute file path of properties file");
		} catch (FileNotFoundException fne) {
			log.error(fne.getMessage(), fne);
			throw new FileNotFoundException();
		} catch (IOException ioe) {
			log.error(ioe.getMessage(), ioe);
			throw new IOException();
		}
	}

	/**
	 * Initialize the deploement process
	 * 
	 * @param propertiesFilePath
	 * @throws Exception
	 */
	public void init(String propertiesFilePath) throws Exception {

		// Load properties file
		DeploymentUtil.loadProperties(propertiesFilePath);

		// Get Standalone environment either 'local' or 'remote' and process
		// accordingly
		String isLocalStandaloneValue = DeploymentUtil
				.getPropertyvalue(Constants.IS_LOCAL_STANDALONE);
		String isRemoteStandaloneValue = DeploymentUtil
				.getPropertyvalue(Constants.IS_REMOTE_STANDALONE);
		String zeroDownTime = DeploymentUtil
				.getPropertyvalue(Constants.ZERO_DOWN_TIME_FLAG);

		// If local standalone flag is not valid throw an exception
		if (!(isLocalStandaloneValue.equals("true") || isLocalStandaloneValue
				.equals("false"))) {
			log.error("Invalid '" + Constants.IS_LOCAL_STANDALONE
					+ "' value in " + propertiesFilePath, new Exception(
					"Invalid '" + Constants.IS_LOCAL_STANDALONE + "' value in "
							+ propertiesFilePath));
			throw new Exception("Invalid '" + Constants.IS_LOCAL_STANDALONE
					+ "' value in " + propertiesFilePath);
		}

		// If remote standalone flag is not valid throw an exception
		if (!(isRemoteStandaloneValue.equals("true") || isRemoteStandaloneValue
				.equals("false"))) {
			log.error("Invalid '" + Constants.IS_REMOTE_STANDALONE
					+ "' value in " + propertiesFilePath, new Exception(
					"Invalid '" + Constants.IS_REMOTE_STANDALONE
							+ "' value in " + propertiesFilePath));
			throw new Exception("Invalid '" + Constants.IS_REMOTE_STANDALONE
					+ "' value in " + propertiesFilePath);
		}

		// If Zero Down Time flag is not valid throw an exception
		if (!(zeroDownTime.equals("true") || zeroDownTime.equals("false"))) {
			log.error("Invalid '" + Constants.ZERO_DOWN_TIME_FLAG
					+ "' value in " + propertiesFilePath, new Exception(
					"Invalid '" + Constants.ZERO_DOWN_TIME_FLAG
							+ "' value in " + propertiesFilePath));
			throw new Exception("Invalid '" + Constants.ZERO_DOWN_TIME_FLAG
					+ "' value in " + propertiesFilePath);
		}

		Boolean isLocalStandalone = Boolean
				.parseBoolean(isLocalStandaloneValue);
		Boolean isRemoteStandalone = Boolean
				.parseBoolean(isRemoteStandaloneValue);

		// If 'local' and 'remote' standalone environments are enabled throw an
		// error
		if ((isLocalStandalone && isRemoteStandalone)
				|| (!isLocalStandalone && !isRemoteStandalone)) {
			String errMesage = "Both '"
					+ Constants.IS_LOCAL_STANDALONE
					+ "' and '"
					+ Constants.IS_REMOTE_STANDALONE
					+ "' properties should not be configured to same status in '"
					+ propertiesFilePath + "'";
			throw new Exception(errMesage);

		// if localStandalone Deployment enabled
		} else if (isLocalStandalone) {

			log.info(":: Local Standalone Deployment ::");
			MuleAppDeploymentStandalone localDeploymentImpl = new MuleAppDeploymentLocalStandaloneImpl();
			// deploy mule applications on local standalone
			localDeploymentImpl.deployMuleApp();

		// if RemoteStandalone Deployment enabled
		} else if (isRemoteStandalone) {
			log.info(":: Remote Standalone Deployment ::");
			MuleAppDeploymentStandalone remoteDeploymentImpl = new MuleAppDeploymentRemoteStandaloneImpl();
			// deploy mule applications on remote standalone
			remoteDeploymentImpl.deployMuleApp();

		}

	}

}
