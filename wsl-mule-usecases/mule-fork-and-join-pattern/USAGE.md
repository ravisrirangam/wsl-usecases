[Purpose](#purpose)  
[Prerequisites](#prerequisites)  
[Initial setup of the Mule project](#initial-setup-of-the-mule-project)  
[Building the demo](#building-the-demo)  



Purpose
=======

This document features MuleSoft's implementation over FORK-AND-JOIN concurrency pattern, the simplest and most efficient way for obtaining 
parallel processing.


Prerequisites
=============

In order to build and run this project you'll need:  

* To download and install [MuleStudio Community edition](http://www.mulesoft.org/download-mule-esb-community-edition).
* A browser to make a request to your Mule application.


Initial setup of the Mule project
=================================
To begin building this application, start Mule Studio and  


### Step 1: Achieve fork-and-join pattern using request-reply.

1. Create an Http-inbound to initiate flow.
2. To fork request to 'n' number of targets specify the targets within all router which is defined inside the request-reply element.
3. Define VM inbound within request-reply element to capture the reposes from the targets with message property MULE_CORRELATION_GROUP_SIZE specifying the number of targets
   to receive response and a collection aggregator to aggregate the responses.  


### Step 2: Configuring the fork targets

1. Define target flows with http inbound and an expression to return a value for each flow.


Building the demo
=================

This demo will be incrementally creating a "Fork-And-Join" pattern support type of application.

### Step 1: Building the "forkAndJoinFlow" flow

1. HTTP inbound Endpoint to invoke the flow.  
2. Add request-reply element to perform the fork and join logic.
3. Define an expression component to perform some operation on aggregate message.


### Step 2: Building the targets "store1Flow,store1Flow" flows  

1. Targets flow start with http inbound and expression component to perform some operation and return the result.


### Step 3: Testing  can observed by invoking forkAndJoinFlow.
