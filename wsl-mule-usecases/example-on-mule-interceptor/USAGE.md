[Purpose](#purpose)  
[Running the application](#running-the-application)  
[Resources](#resources)

Purpose
===========

This document illustrates the concept of **Mule Interceptor** with a basic usecase.


**Use-case**: Trigger the flow with Http and intercept the current payload of the flow using  **Interceptor** interface and calculate the time took for flow execution.

Running the application
=======================

1. Right click on example-on-mule-interceptor.mflow and select **Run As Mule Application**.
2. Check the console to see output when the application starts.

	++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	+ Started app 'example-on-mule-interceptor'		           +
	++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
3. Hit the endpoint at<http://localhost:8081/muleInterceptor>.
	Note: current payload to the flow would be '/muleInterceptor'
	
4. you would be seeing below logs at the console

		:::in process: payload before intercept:::::::/muleInterceptor::::::::::
		:::in process: payload after intercept:::::::/muleInterceptor!::::::::::
		interceptorFlow, /muleInterceptor!

5. Observe the current payload which is **/muleInterceptor!** 

		**!** is added at end of the payload by the Class 'MyCustomInterceptor'

6. You would also see the below log - due to timer-interceptor

		interceptorFlow took 18ms to process event [0-287071b8-ba29-11e3-8eed-0d942f57ad4d]

	
Resources
===========

● [Using Interceptors] (http://www.mulesoft.org/documentation/display/current/Using+Interceptors)

● [What are Interceptors?] (http://ricston.com/blog/interceptors/)

● [Example on Interceptor Flow Test Case] (https://gist.github.com/cmordue/4552292)
	

