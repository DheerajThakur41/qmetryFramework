package com.qaf.api.test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.qaf.api.utils.DataUtil;
import com.qmetry.qaf.automation.step.WsStep;
import com.qmetry.qaf.automation.ws.rest.RestTestBase;
import com.sun.jersey.api.client.ClientResponse;

public class EventTestApi extends BaseTest{

	Map<String,Object> data=new HashMap<String,Object>();

	@Test(description = "List All Event",enabled=true)
	public void listofallEvents() {
		String token = DataUtil.getData("admin.token");
		data.put("token", token);

		ClientResponse res = WsStep.userRequests("list.Events",data);//
		if(res.getStatus()==200) {
		String messageBody = new RestTestBase().getResponse().getMessageBody();
		DocumentContext jsonContext = JsonPath.parse(messageBody);

		String eventNameJsonPath = "$.data[*].attributes.name";
		List<String> eventNameList = jsonContext.read(eventNameJsonPath);

		for (String eventName : eventNameList) {
			System.out.println("Name: "+eventName);
		}}
		else {
			throw new RuntimeException("Failed Http error Code"+res.getStatus());
		}
	}
	
	@Test(description = "Create New Event",enabled=true) 
	public void createNewEvents() { 
		
	  ClientResponse res = WsStep.userRequests("create.Event",data);
	  if(res.getStatus()==201) {
	  WsStep.responseShouldHaveHeaderWithValue("Content-Type","application/vnd.api+json"); 
	  String messageBody = new RestTestBase().getResponse().getMessageBody(); 
	  DocumentContext jsonContext = JsonPath.parse(messageBody);
	  
	  String eventNameJsonPath = "$.data.id"; 
	  String createdEventId =jsonContext.read(eventNameJsonPath);
	  System.out.println("Created Event: "+createdEventId);
	  DataUtil.updateData("new.EventID", createdEventId); 
	  }else {
		  Assert.assertFalse(true);
	  }
	}
	
	@Test(description = "get Event Details",enabled=true)
	public void getEventDetais() {
		data.put("new_Event_ID",DataUtil.getData("new.EventID"));

		WsStep.userRequests("get.eventDetail",data);//
		WsStep.responseShouldHaveStatusCode(200);
		WsStep.responseShouldHaveHeaderWithValue("Content-Type", "application/vnd.api+json");
		
		String messageBody = new RestTestBase().getResponse().getMessageBody();
		DocumentContext jsonContext = JsonPath.parse(messageBody);
		
		String eventNameJsonPath = "$.data.id";
		String createdEventId = jsonContext.read(eventNameJsonPath);
		System.out.println("Created Event: "+createdEventId);
	}
	
	@Test(description = "delete Event",enabled=true)
	public void deleteEvent() {
		
		data.put("new_Event_ID",DataUtil.getData("new.EventID"));

		WsStep.userRequests("delete.event",data);//
		WsStep.responseShouldHaveStatusCode(200);
		WsStep.responseShouldHaveHeaderWithValue("Content-Type", "application/vnd.api+json");
		WsStep.responseShouldHaveKeyAndValueContains("Object successfully deleted","meta.message");
	}
}

