[Purpose](#purpose)  
[Prerequisites](#prerequisites)  
[Initial setup of the Mule project](#initial-setup-of-the-mule-project)  
* [Step 1: Install ](#step-1-install)  
* [Step 2: Create a new Mule project](#step-2-create-a-new-mule-project)  
* [Step 3: Store the credentials](#step-3-store-the-credentials)  
* [Step 4: Create a Global element](#step-4-create-a-global-element)
[Running the application](#running-the-application)  
[Resources](#resources)

Purpose
=======

This document features how to configure ftp connector, override the poll method to pick all the files available in the ftp target directory.

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



