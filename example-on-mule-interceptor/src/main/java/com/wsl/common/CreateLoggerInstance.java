package com.wsl.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class CreateLoggerInstance {
List<Object> rNotificationArrayL = new ArrayList<Object>();
	
	public static Logger log = Logger.getLogger(CreateLoggerInstance.class.getName());
	
}
