#Testing approach for JMS bases Mule Project

[Purpose](#purpose)

[Mflow file](#mflow-file)

[Unit Testing and Mocking](#unit-testing-and-mocking)

[Test Mule Project](#test-mule-project)

Purpose
=======

	* The purpose of this document gives an overview on mocking and unit testing the mule project when it contains the JMS as outbound endpoint.
	
Mflow file
===========
	* Consider the mflow which contains the http inbound,append string message processor,jms outbound endpoint and object to string message processor.

Unit Testing and Mocking using Junit Test Case
================================================
	* Create a Junit Test case under src/main/java folder and name of the test class should end with Test.
	* Import the munit related packages into test class.
	* configure the getresourceconfiguration method by pointing to configuration file.
	* Create test method which is used to invoke the flow.Pass the flow name as input parameter
	* Create test methods which covers the possitve,negative and null payload use case.
	* To mock the JMS outbound can use the below syntax.
		whenEndpointWithAddress("jms://<QueueName>").thenReturn(mulemessageWithPayload("<pass the data here>"));
		
Test Mule Project
==================
	* Right click on the Junit Test case and run as Junit Test case.
	* Open the Junit console and see the test results.

For more understanding can refer the attached jmsoutbound sample code.
	
