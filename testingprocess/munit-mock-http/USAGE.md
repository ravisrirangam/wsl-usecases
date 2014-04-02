[Purpose](#purpose)  
[Project Description](#description)  
[Procedure to Mock DB Endpoint](#procedure-to-mock-db-endpoint)  
[Test the application](#test-the-application)  


Purpose
===========

This document illustrates the concept of **Mocking** and **Unit-Testing** of **Http-outbound-Endpoint** with a basic usecase.
Mocking is achieved using MUnit Framework.

**Use-case**: Instead of obtain response from REST or SOAP web-service(s), **mock Http-outbound-endpoint** and do **Unit-Testing**.

Description
========================

Project named **munit-mock-http**, includes two Mflows below

● munitMockHTTP-REST.mflow

● munitMockHTTP-SOAP.mflow

**munitMockHTTP-REST.mflow**, includes following flows

	1. munit-http-RestConsumer: Starts with HTTP inbound-endpoint and followed with Http outbound-endpoint(pointing to Rest Webservice) expecting JSON response.

	2. munit-http-Rest-XML-Consumer: Starts with HTTP inbound-endpoint and followed with Http outbound-endpoint(pointing to Rest Webservice) expecting XML response.

**munitMockHTTP-SOAP.mflow**, includes following flows

	1. munit-http-SoapConsumer: Starts with HTTP inbound-endpoint and followed with Http outbound-endpoint(pointing to SOAP Webservice).


Procedure to Mock DB Endpoint
================================================

Syntax:

		whenEndpointWithAddress("<address>").thenReturn( muleMessageWithPayload( <method to return results> ) );
		

Example:

		whenEndpointWithAddress("http://localhost:8686/ActualRestServer/RestServerClass?param1=Team").thenReturn(muleMessageWithPayload(ResponseXmlObject.getXMLData("Team mock")));

		
Unit Testing and Mocking
================================================

	● Create a Junit Test case under src/test/java folder and name of the test class should end with Test.

	● Import the munit related packages into test class.

	● Configure the getresourceconfiguration method by pointing to configuration file.

	● Create test method which is used to invoke the flow.Pass the flow name as input parameter.

	● Create test methods which covers the possitve,negative and null payload use case.


Test the application
==================
	* Right click on the Junit Test case and run as Junit Test case.
	* Open the Junit console and see the test results.

For more understanding can refer the attached 'munit-mock-http' sample code.
