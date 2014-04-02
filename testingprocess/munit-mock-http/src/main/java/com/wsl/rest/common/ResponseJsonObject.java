package com.wsl.rest.common;

import org.json.simple.JSONObject;

public class ResponseJsonObject {
	/**
	 * @param param1
	 * 
	 * @return a org.json.simple.JSONObject as
	 *  {"balance":1000.21,"num":100,"is_vip":true,"name":"Team mock"}
	 */
	@SuppressWarnings("unchecked")
	public static Object getJSONData(String param1)
	{
		  JSONObject obj = new JSONObject();
	      obj.put("name", param1);
	      obj.put("num", new Integer(100));
	      obj.put("balance", new Double(1000.21));
	      obj.put("is_vip", new Boolean(true));
		return obj;
	}
}
