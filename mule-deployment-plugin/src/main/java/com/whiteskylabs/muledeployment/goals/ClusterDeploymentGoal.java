package com.whiteskylabs.muledeployment.goals;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.whiteskylabs.muledeployment.mmc.InvokeMMCDeployment;

/**
 * Goal which starts mule applications deployment on 'Cluster'.
 * 
 * @goal cluster
 * 
 * @phase process-sources
 */
public class ClusterDeploymentGoal extends AbstractMojo {

	/**
	 * @parameter expression = "${propertiesFilePath}"
	 */
	private String propertiesFilePath;
	static Logger log = Logger.getLogger(ClusterDeploymentGoal.class
			.getName());
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		try {
			
			
			PropertyConfigurator
					.configure("./conf/log4j/mmc/log4j.properties");
			log.info("================================================================================");
			log.info("Deployment process started on 'Cluster'");
			log.info("================================================================================");
			log.debug("propertiesFilePath : " + propertiesFilePath);
			// If 'propertiesFilePath' parameter not provided throw NullPointerException
			if (propertiesFilePath == null) {
				throw new NullPointerException(
						"Please provide 'propertiesFilePath' parameter with absolute file path of properties file");
				
			// else process the deployment
			} else {
				String[] mainMethodArgs = { propertiesFilePath };
				
				InvokeMMCDeployment.isDeploymentTargetCluster = true;
				// Invoke MMC deployment process
				InvokeMMCDeployment.main(mainMethodArgs);
			}
			
			log.info("================================================================================");
			log.info("Deployment process completed");
			log.info("================================================================================");
		} catch (NullPointerException npe) {
			log.error(npe.getMessage(), npe);
			throw new NullPointerException(npe.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new MojoExecutionException(e.getMessage(), e);
		}

	}

}
