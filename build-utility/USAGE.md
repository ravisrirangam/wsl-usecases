[Purpose](#purpose)
[Prerequisites](#prerequisites)
[Project setup](#project-setup)
* [Step 1: Create a Configuration project](#step-1-create-a-configuration-project)  
* [Step 2: Create a new Mule project](#step-2-create-a-new-mule-project)  

Purpose
=======

The Mule projects developed using the Mule Studio need to be promoted to various environments, such as Development, Test, Performance, UAT and Production. 
The intention is to make the Mule projects environment agnostic and have the build process update the properties per the target environment.

Prerequisites
=============

The proposed build process requires two maven plugins, namely - maven-dependency-plugin and maven-resources-plugin. 

Project setup
==============
### Step 1: Create a Configuration project

To begin building this application, start Mule Studio and

1. Go to create a new Maven project.
2. Name of this project should reflect with <FunctionalityName>-Configuration.
3. Create a configs folder under src/main/resources which contains property files of all environments.Ex:dev,sit,uat,pref and prod.
4. Use Constants.java and UpdateMuleDeployProperties.java as same and keep these two utilities under src/main/java floder.
5. Use the existing pom.xml file and change the artifactId and groupId according to the requirement.
6. Open command prompt and point to project location.
7. Execute the below command.

	mvn clean install -DmuleProjectDirectory=<path where mule project is located>  -Dskip.updatedeployproperties=false

### Step 2: Create a new Mule project

1. Create a new pom based Mule project.
2. Add the goals unpack,read-project-properties and run to pom.xml file.(Can refer 36-62 ,124-141 and 279-299 lines of pom.xml).
3. create a properties file on local system(Ex:maven.properties).This property file contains the names of property files  to be replaced with environment specific values.
4. Open command prompt and point to project location
5. Execute the below command.

	mvn compile package –Denvironment.name=dev -DFile=F:/maven.properties
	
After executing the above command mule project contains the property files with values related to dev environment









