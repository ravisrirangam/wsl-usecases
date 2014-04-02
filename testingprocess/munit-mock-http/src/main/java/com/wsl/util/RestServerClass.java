package com.wsl.util;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.wsl.rest.common.ResponseJsonObject;
import com.wsl.rest.common.ResponseXmlObject;

@Path("/")
public class RestServerClass {
	@GET
	@Path("RestServerClass")
	@Produces("text/xml")
	public Object getsampleXML(@QueryParam("param1")String param1)
	 {
		System.out.println("from Service: "+this.getClass().toString()+" getsampleXML Method");
		return ResponseXmlObject.getXMLData(param1);
	 }
	
	@POST
	@Path("RestServerClass")
	@Produces("application/json")
	public Object getSampleJson(@QueryParam("param1")String param1)
	 {
		System.out.println("from Service: "+this.getClass().toString()+" getSampleJson Method");
		return ResponseJsonObject.getJSONData(param1);
	 }
	
	
}
