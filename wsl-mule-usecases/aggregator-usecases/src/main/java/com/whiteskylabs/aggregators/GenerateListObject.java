package com.whiteskylabs.aggregators;

import java.util.ArrayList;
import java.util.List;

public class GenerateListObject {

	public List<String> getListObject(String payload){
		
		List<String> list = new ArrayList<String>();
		list.add("first");
		list.add("second");
		list.add("third");
		
		return list;
	}
	
}
