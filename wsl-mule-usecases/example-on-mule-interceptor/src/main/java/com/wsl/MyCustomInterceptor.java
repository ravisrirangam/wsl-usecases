package com.wsl;
import org.apache.log4j.PropertyConfigurator;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.interceptor.Interceptor;
import org.mule.processor.AbstractInterceptingMessageProcessor;

import com.wsl.common.CreateLoggerInstance;
 
public class MyCustomInterceptor extends AbstractInterceptingMessageProcessor implements Interceptor
{
    
    @Override
    public MuleEvent process(MuleEvent event) throws MuleException
    {
    	PropertyConfigurator.configure("./config/log4j.properties");
    	String payload = (String)event.getMessage().getPayload();
    	CreateLoggerInstance.log.info(":::in process: payload before intercept:::::::"+event.getMessage().getPayload().toString()+"::::::::::");
    	event.getMessage().setPayload(payload + "!");
    	CreateLoggerInstance.log.info(":::in process: payload after intercept:::::::"+event.getMessage().getPayload().toString()+"::::::::::");
        return processNext(event);
    }
 
}