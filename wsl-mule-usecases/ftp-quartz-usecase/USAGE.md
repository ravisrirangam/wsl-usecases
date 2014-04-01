[Purpose](#purpose)  

[Prerequisites](#prerequisites) 

[Initial set up of the Mule project](#initial-set up-of-the-mule-project)

[Running Mule project](#running-mule-project)

Purpose
=======

This document features how to configure ftp connector, override the poll method of FTPMessageReceiver to pick all the files available in the ftp target directory.

Prerequisites
=============

In order to build and run this project you'll need:  

* Local FTP Server. This can be achieved through FileZilla Server.
  Need to create a user and target ftp Directory.
  The same ftp credential needs to be added to FTP end point in mule project.

Initial set up of the Mule project
=================================

### Step 1: Quartz Connector Configuration

1. Create Quartz global connector.
2. In the advanced tab add factory properties 'org.quartz.threadPool.threadCount' and set to 1.
   This ensures at any time only single quartz thread is active.

### Step 2: FTP Connector Configuration

1. Create FTP global connector.
2. In the connector add receiver threading profiler and set maxThreadsActive="1".
3. In service-overrides change  messageReceiver, messageFactory to custom java classes.
4. These classes are available at folder org.mule.transport.ftp.MyCustomFTPMessageReceive,    org.mule.transport.ftp.MyMessageFactory respectively.
5. The purpose of 'MyCustomFTPMessageReceive' is it overrides the poll method of FTPMessageReceiver and facilitates picking all the files one time available in ftp target directory instead of picking them one by one. This method returns a List of byte array where each byte array is the file payload instead of returning a FTPFile as done by the conventional FTPMessageReceiver.
6.On the other hand 'MyMessageFactory' allows to create mule message of type List where the traditional MessageFactory supports only type 'FTPFile'.
7. Groovy scripts are used to disable polling through FTP inbound and to poll through Quartz trigger only.

Running Mule project
=================================
1. When the application is started, the Quartz endpoint with name 'StopFTP' stops the connector. This helps in avoiding any processing or picking of the file during application start up.
2. The second Quartz end point 'Trigger' actually triggers the flow. Its frequency is set to 1 minute. It starts the ftp connector and after 3s stops the connector.
3. During this time interval ftp connector picks up all the file at one go and even if the connector is stopped the file processing still continues.

