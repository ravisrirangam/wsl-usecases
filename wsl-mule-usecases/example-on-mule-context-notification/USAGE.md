[Purpose](#purpose)  
[Running the application](#running-the-application)  
[Resources](#resources)

Purpose
===========

This document illustrates the concept of **Mule Context Notification** with a basic usecase.


**Use-case**: Trigger the flow(s) automatically when the mule context has been started using **MuleContextNotificationListener** interface

Running the application
=======================

1. Right click on MuleContextNotificationExample.mflow and select **Run As Mule Application**.
2. Check the console to see output when the application starts.

	++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	+ Started app 'example-mule-context-notification'          +
	++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	From flow: MuleContextNotificationExampleFlowVM, Current Payload:This is payload sent in Context

3. Note that the line starting with From flow are logged due to **auto trigerring** of the flow by name **MuleContextNotificationExampleFlowVM**

4. You can also see the logs below, which indicates that 'MuleCustomContextNotification' class is able to capture the action items realted to "MuleContextNotifications" 
	and based on the action **MuleContextNotification.CONTEXT_STARTED** automatically flow are getting invoked.

		MuleCustomContextNotification:33 - --------MuleCustomContextNotification------------------
		MuleCustomContextNotification:34 - getAction 104, MuleContextNotification.CONTEXT_STARTED 104
		MuleCustomContextNotification:35 - getActionName mule context started
		MuleCustomContextNotification:36 - getType info
		MuleCustomContextNotification:37 - getClass class org.mule.context.notification.MuleContextNotification
		MuleCustomContextNotification:38 - getSource rafiqoffice-pc..example-mule-context-notification
		MuleCustomContextNotification:39 - EVENT_NAME MuleContextNotification
		MuleCustomContextNotification:40 - --------------------------
		MuleCustomContextNotification:41 - ::::::notificationArrayL.size::::::::::::2::::::::::::::::::
		MuleCustomContextNotification:59 - :::::::::::::::::::::mule context started:::::::::::::::::::::
		AbstractLifecycleManager:197 - Initialising: 'connector.VM.mule.default.dispatcher.694395027'. Object is: VMMessageDispatcher
		AbstractLifecycleManager:197 - Starting: 'connector.VM.mule.default.dispatcher.694395027'. Object is: VMMessageDispatcher

Resources
===========

●     [Mule Server Notifications] (http://www.mulesoft.org/documentation/display/current/Mule+Server+Notifications)
●     [System notifications Mule] (http://ricston.com/blog/system-notifications-mule/)
●     [Server Notification Listener Interface] (http://ricston.com/blog/servernotificationlistener-interface/)
●     [Listening Notifications] (http://ricston.com/blog/listening-notifications/)

