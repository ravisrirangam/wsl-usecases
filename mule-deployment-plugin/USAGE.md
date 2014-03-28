#Mule Deployment Plugin 

This Mule deployment plugin will allow users to deploy multiple mule applications on the MMC and Standalone by executing below maven commands. 

	* mvn wsldeploy:mmc  -- To deploy Mule applications on MMC server.
	* mvn wsldeploy:standalone -- To deploy Mule application on Mule Standalone server.

#Following operations get executed by this plugin.
	* Undeploy: Undeploys and deletes the existing application (Which has the same name as new application will be deploy) on the server.
	* Clear Cache: Clears the Cache of the undeployed application. (This operation can be switched off by setting the 'XXXXClearCacheStatus' value 'false' in the corresponding properties file)
	* Delete Log Files: Deletes the log files of undeployed application. (This operation can be switched off by setting the 'XXXXClearCacheStatus' value 'false' in the corresponding properties file)
	* Deploy: Deploys the new mule applications on the server.
	
#Steps to use 'Mule Deployment Plugin':
Following steps need to take to deploy mule applications on mule server using this plugin

	1) After cloning the 'mule-deployment-plugin' from this repository, Add below 'pluginGroup' as child element to 'pluginGroups' element in MAVEN_HOME/conf/setting.xml file. 'groupId' (com.whiteskylabs.muledeployment.goals) of the 'mule-deployment-plugin' should be added to 'pluginGroup' element. 
			<pluginGroups>
                 <pluginGroup>com.whiteskylabs.muledeployment.goals</pluginGroup>
            </pluginGroups>    
	2) Do mvn install from the location of 'mule-deployment-plugin'. So that, plugin get installed on maven local repository.
	3) Place all your mule applications (.zip files need to be deployed) in a directory of local system and set this directory location to â€œmuleApplicationsSourceDirectoryâ€� key in the XXXXDeployment.properties file.
	4) Configure target Mule server (MMC or Standalone) details and other configurations in XXXXDeployment.properties file. (Described each property below and attached sample properties files to this page) 
	5) Pass the absolute file path of the properties file as a parameter while executing the maven command.
	6) Execute the Maven command
		* Maven command for MMC:       mvn wsldeploy:mmc -DpropertiesFilePath=G:\mulesoft\MuleAppDeployment\MMCDeployment.properties
		* Maven command for Mule Standalone server:  mvn wsldeploy:standalone -DpropertiesFilePath=G:\mulesoft\MuleAppDeployment\StandaloneDeployment.properties