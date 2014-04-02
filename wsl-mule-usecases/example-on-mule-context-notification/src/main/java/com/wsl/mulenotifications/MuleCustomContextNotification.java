package com.wsl.mulenotifications;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.api.context.notification.MuleContextNotificationListener;
import org.mule.context.notification.MuleContextNotification;

/**
 * @author Mohammad Rafiq
 * 
 */

public class MuleCustomContextNotification implements
		MuleContextNotificationListener<MuleContextNotification> {

	
	List<Object> notificationArrayL = new ArrayList<Object>();
	private static Logger log = Logger.getLogger(MuleCustomContextNotification.class.getName());

	/* (non-Javadoc)
	 * @see org.mule.api.context.notification.ServerNotificationListener#onNotification(org.mule.api.context.notification.ServerNotification)
	 */
	public void onNotification(final MuleContextNotification notification) {
		
		PropertyConfigurator.configure("./config/log4j.properties");
		

		notificationArrayL.add(notification);
		log.info("--------MuleCustomContextNotification------------------");
		log.info("getAction " + notification.getAction()+", MuleContextNotification.CONTEXT_STARTED "+MuleContextNotification.CONTEXT_STARTED); 
		log.info("getActionName " + notification.getActionName());
		log.info("getType " + notification.getType());
		log.info("getClass " + notification.getClass());
		log.info("getSource " + notification.getSource().toString());
		log.info("EVENT_NAME " + notification.EVENT_NAME);
		log.info("--------------------------");
		log.info("::::::notificationArrayL.size::::::::::::" + notificationArrayL.size()+ "::::::::::::::::::");

		if (notification.getAction() == MuleContextNotification.CONTEXT_STARTED) {
			sendNotificationToFlow(notification);
		}
		
	}

	
	/**
	 * @param notification
	 */
	private void sendNotificationToFlow(final MuleContextNotification notification) {
		
		try {
			
			MuleMessage message = new DefaultMuleMessage(notification,notification.getMuleContext());
			message.setPayload("This is payload sent in Context");
			log.info(":::::::::::::::::::::"+notification.getActionName()+":::::::::::::::::::::");
			if (notification.getAction() == MuleContextNotification.CONTEXT_STARTED) {
				notification.getMuleContext().getClient().send("vm://vmQFlow1", message);
			}
		} catch (Exception exec) {
			log.error("Exception:::::::::::::::::::"+exec.getStackTrace().toString());
		}
	}

	
}
