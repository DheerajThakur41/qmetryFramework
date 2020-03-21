package com.qaf.api.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.qaf.api.utils.DataUtil;
import com.qmetry.qaf.automation.step.WsStep;
import com.qmetry.qaf.automation.ws.rest.RestTestBase;

public class TestEventTopicApi extends BaseTest{

	Map<String,Object> data=new HashMap<String,Object>();

	@Test(description = "get List of Event Topic",enabled=true)
	public void getListofEventTopic() {
		String token = DataUtil.getData("admin.token");
		data.put("token", token);
		
		WsStep.userRequests("list.EventsTopic",data);//
		WsStep.responseShouldHaveStatusCode(200);
		WsStep.responseShouldHaveHeaderWithValue("Content-Type", "application/vnd.api+json");
		String messageBody = new RestTestBase().getResponse().getMessageBody();
		DocumentContext jsonContext = JsonPath.parse(messageBody);

		String eventNameJsonPath = "$.data[*].attributes.name";
		List<String> eventTopicNameList = jsonContext.read(eventNameJsonPath);

		for (String eventTopicName : eventTopicNameList) {
			System.out.println("Name: "+eventTopicName);
		}
	}

	@Test(description = "create Event Topic",enabled=true)
	public void createEventTopic() {

		WsStep.userRequests("create.EventTopic",data);//
		WsStep.responseShouldHaveStatusCode(201);
		WsStep.responseShouldHaveHeaderWithValue("Content-Type", "application/vnd.api+json");
		WsStep.responseShouldHaveKeyAndValueContains("event-topic","data.type");
		
		String messageBody = new RestTestBase().getResponse().getMessageBody();
		DocumentContext jsonContext = JsonPath.parse(messageBody);

		String eventTopicIdjpath = "$.data.id";
		String eventTopicTypejpath = "$.data.type";
		String eventTopicid = jsonContext.read(eventTopicIdjpath);
		String eventTopicType = jsonContext.read(eventTopicTypejpath);

		DataUtil.updateData("new.eventTopicID", eventTopicid);
		System.out.println("Event Topic ID:"+eventTopicid+"\nEvent Topic Type:"+eventTopicType);
	}
	
	@Test(description = "Get Event Topic Details",enabled=true)
	public void eventTopicDetails() {
		
		data.put("event_topic_id", DataUtil.getData("new.eventTopicID"));

		WsStep.userRequests("get.EventTopicDetail",data);
		WsStep.responseShouldHaveStatusCode(200);
		WsStep.responseShouldHaveHeaderWithValue("Content-Type", "application/vnd.api+json");
		WsStep.responseShouldHaveValueAtJsonpath("event-topic","data.type");
		WsStep.responseShouldHaveValueAtJsonpath(DataUtil.getData("new.eventTopicID"),"data.id");
		
	}
	
	@Test(description = "Delete Event Topic Details",enabled=true)
	public void deleteEventTopic() {
		
		data.put("event_topic_id",  DataUtil.getData("new.eventTopicID"));

		WsStep.userRequests("delete.EventTopicDetail",data);
		WsStep.responseShouldHaveStatusCode(200);
		WsStep.responseShouldHaveHeaderWithValue("Content-Type", "application/vnd.api+json");
		WsStep.responseShouldHaveValueAtJsonpath("Object successfully deleted","meta.message");
	}
	
}
