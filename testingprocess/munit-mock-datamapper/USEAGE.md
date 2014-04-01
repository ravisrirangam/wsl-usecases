[Purpose](#purpose)  
[Test the application](#test-the-application)  

Purpose
===========

This document illustrates the concept of **Mocking Data-Mapper** with a basic usecase. Mocking is achieved using MUnit Framework.


**Use-case**: Trigger the flow with HTTP Inbound endpoint and transform current payload to xml using **Data-Mapper(mock)**.




**include .grf files to classpath**

	While mocking Data-mapper all the ".grf" files should available to class path. So, right click on "mappings" folder
	hover on 'Build Path' and select "Use as Source Folder" option.

Test the application
=======================

1. Navigate to src/test/java folder
2. Right click on **DataMapperMunitMockingTest.java** and select **Run As JUnit Test**.
3. Switchover to JUnit wundow to see the output.
