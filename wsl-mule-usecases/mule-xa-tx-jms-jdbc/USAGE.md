[Purpose](#purpose)  
[Prerequisites](#prerequisites)  
[Initial setup of the Mule project](#initial-setup-of-the-mule-project)  
[Building the demo](#building-the-demo)  



Purpose
=======

This document features MuleSoft's XA transactions and displays how to meet configuration requirements for using the XA transactions in a Mule project.


Prerequisites
=============

In order to build and run this project you'll need:  

* An Oracle XE database installed instance with an existence of table USER_DATA with fields(ID,NAME,AGE,EMP_ID).
* To download and install [MuleStudio Community edition](http://www.mulesoft.org/download-mule-esb-community-edition).
* A browser to make a request to your Mule application.
* Jar support(activemq-all,ojdbc6,ucp).


Initial setup of the Mule project
=================================
To begin building this application, start Mule Studio and  

### Step 1: Configuring the mule inbuilt in-memory activemQ connection factory.

1. Define the global activemq-xa-connector  with connectionfactory reference to XA supported spring reference bean with class ActiveMQXAConnectionFactory.
2. Also define activemQ brooker through spring bean which refers to activemQ policies map.  
3. Configure DLQ strategy support.   

### Step 2: Configuring the mule jdbc connector

1. Define the global jdbc-ee connector  with data-source reference to XA supported spring reference poolXA data source bean with class PoolXADataSourceImpl.
2. Also define activemQ brooker through spring bean which refers to activemQ policies map.  
3. Configure DLQ strategy policy.

### Step 3: Configuring global jbossts transaction-manager

1. Define the global jbossts transaction-manager to XA transactions.



Building the demo
=================

This demo will be incrementally creating a "XA transaction" support type of application.Element property-placeholder
allows spring property placeholders to be configured directly in the Mule configuration file.

### Step 1: Building the "xajmsdbtaskFlow-addpayloadto-jms" flow

1. HTTP Inbound Endpoint to invoke the flow.  
2. Add set-payload Element that sets the paylaod with the provided value next to http inbound.
3. Filter the Palette by "JMS" component with queue name "testDataQueue" and add next to the set-payload Element.  


### Step 2: Building the "xajmsdbtaskFlow-XA-jmsto-db" flow  

1. Define JMS inbound which support XA transaction.
2. Use data mapper to transform the payload to list of map objects.
3. Add DB component to the outbound endpoint which uses the transform message inserted into respective database table.



### Step 3: Testing of XA transaction can observed by playing with jdbc outbound-endpoint with valid /invalid query key value with respective transaction levels.



