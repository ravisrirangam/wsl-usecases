package com.whiteskylabs.utils.build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import org.apache.log4j.Logger;

public class UpdateMuleDeployProperties {

	static Logger log = Logger.getLogger(UpdateMuleDeployProperties.class
			.getName());

	public static void main(String args[]) throws IOException {
		/*
		 * Method initiate the check and updation process  
		 */		
		initUpdatePropertiesfile(args);

	}

	
	/* Method checks the input parametrs and update the mule-deploy.properties file
	 * @param args 
	 * Expects two arguments
	 * args[0] specifies muleProjectDirPath 
	 * args[1] specifies skipUpdateMDeployproperties
	 */
	private static void initUpdatePropertiesfile(String args[]) {

		try {
			if (args.length > 0 && args[0] != null && args[1] != null) {
				
				log.info("--------------------into UpadteMuleDeploy properties class----------------------------------");
				if (args[1].equalsIgnoreCase("false")) {
					//	Method accepts the input parameter as muleproject directopry path and initiates the updating  mule-deploy.properties files				
					updateMuleDeployProperties(args[0]);
				} else {
					log.info("skiped updating MUledeployproperties");
					throw new Exception(
							"Invalid 'skip.updatedeployproperties' argument value passed expecting either true/false as argument value");

				}
			} else {
				throw new Exception("No proper arguments passed");

			}
		} catch (Exception ex) {
			log.error("No proper arguments passed");
			ex.printStackTrace();
			System.exit(0);
		}
	}

	
	/** Below method updates the mule-deploy.properties files
	 * @param projectPath Mule project directory 
	 */
	private static void updateMuleDeployProperties(String projectPath) {

		// Locating the path to mule project flow xml's directory

		String[] extensions = new String[] { "xml", "XML" };
		File flowsFolder = new File(projectPath
				+ Constants.MULE_APP_FLOW_XMLS_DIR);
		try {
			if (flowsFolder.exists()) {

				// Listing all the flow xml's in the directory
				List<File> listFlowXmlFiles = (List<File>) FileUtils.listFiles(
						flowsFolder, extensions, true);
				String configResources = "";

				// Listing all the xml file names and append to string

				for (int i = 0; i < listFlowXmlFiles.size(); i++) {

					if (i == (listFlowXmlFiles.size() - 1))
						configResources += listFlowXmlFiles.get(i).getName();
					else
						configResources += listFlowXmlFiles.get(i).getName()
								+ ",";
				}
				Properties prop = new Properties();
				InputStream input = null;
				try {
					
					// Locating the mule deploy properties file
					File muleDeployPropertiesFile = new File(
							projectPath
									+ Constants.MULE_APP_MULE_DEPLOY_PROPERTIES_FILE_LOCATION);

						// Checking the permissions for mule-deploy.properties
					if (muleDeployPropertiesFile.canWrite()) {
						input = new FileInputStream(muleDeployPropertiesFile);

						// Load the mule-deploy.properties
						prop.load(input);
						prop.setProperty("config.resources", configResources);
						prop.store(
								new FileOutputStream(
										projectPath
												+ Constants.MULE_APP_MULE_DEPLOY_PROPERTIES_FILE_LOCATION),
								null);
					} else {
						throw new Exception(
								"Permission denied to update mule deploy properties File:"
										+ muleDeployPropertiesFile);
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(0);
				} finally {
					if (input != null) {
						try {
							input.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				log.error("Invalid mule project directory");
				throw new Exception("Invalid mule project directory");

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}

	}
}
