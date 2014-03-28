package com.whiteskylabs.muledeployment.goals;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import com.whiteskylabs.muledeployment.standalone.InvokeStanaloneDeployment;

/**
 * Goal which starts mule applications deployment on 'Standalone'.
 * 
 * @goal standalone
 * 
 * @phase process-sources
 */
public class StandaloneDeploymentGoal extends AbstractMojo {
	/**
	 * @parameter expression = "${propertiesFilePath}"
	 */
	private String propertiesFilePath;
	static Logger log = Logger.getLogger(StandaloneDeploymentGoal.class
			.getName());

	public void execute() throws MojoExecutionException {

		try {
			PropertyConfigurator
					.configure("./conf/log4j/standalone/log4j.properties");

			log.info("================================================================================");
			log.info("Deployment process started on 'Standalone' server");
			log.info("================================================================================");
			log.debug("propertiesFilePath : " + propertiesFilePath);

			// If 'propertiesFilePath' parameter not provided throw
			// NullPointerException
			if (propertiesFilePath == null) {
				throw new NullPointerException(
						"Please provide 'propertiesFilePath' parameter with absolute file path of properties file");

				// else process the deployment
			} else {
				String[] mainMethodArgs = { propertiesFilePath };

				// Invoke MMC deployment process
				InvokeStanaloneDeployment.main(mainMethodArgs);
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
