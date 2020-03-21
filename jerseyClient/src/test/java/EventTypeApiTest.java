import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import util.DataUtil;

public class EventTypeApiTest {

	
	@Test(description = "List All Event",enabled=true,priority = 0)
	public void ListOfAllEventType() {
		Client client = Client.create();
		WebResource uri = client.resource(DataUtil.readData("BASE_URI")).path("/v1/event-locations");
		
		ClientResponse resp = uri.queryParam("sort","email")
								.header("Content-Type", "application/vnd.api+json")
								.header("Authorization", CommonApi.generateToken())
								.get(ClientResponse.class);
		
		String respBody = resp.getEntity(String.class);
		
		DocumentContext jsonContext = JsonPath.parse(respBody);
		String eventNameJsonPath = "$.data[*].attributes.name";
		List<String> eventNameList = jsonContext.read(eventNameJsonPath);
		
//		assertThat(eventNameList.toString(), containsString("infostretch"));
		
		System.out.println("####List of Event Name#####");
		for (String eventName : eventNameList) {
			System.out.println("Name: "+eventName);
		}
		System.out.println("*****Response Body*****\n"+DataUtil.toPrettyFormat(respBody));
	}	
	
	@Test(description = "Create New Event",enabled=true,priority = 1)
	public void createNewEventsType() {
		Client client = Client.create();
		WebResource uri = client.resource(DataUtil.readData("BASE_URI")).path("/v1/event-types");
		
		// defining payload body data
		JsonObject payload = new JsonObject();
		JsonObject data = new JsonObject();
		
		JsonObject attribute = new JsonObject();
		attribute.addProperty("name", DataUtil.readData("newEvent.name"));
		
		data.add("attributes", attribute);
		data.addProperty("type", DataUtil.readData("newEvent.type"));
		payload.add("data", data);
		String payloaddata=payload.toString();
		
		ClientResponse resp = uri.header("Content-Type", "application/vnd.api+json")
								.header("Authorization", CommonApi.generateToken())
								.post(ClientResponse.class,payloaddata);
		
		String respBody = resp.getEntity(String.class);
		System.out.println("*****Response Body*****\n"+DataUtil.toPrettyFormat(respBody));
		
		//verifications
		Assert.assertEquals(resp.getStatus(), 201);
		String eventNameJsonPath = "$.data.id";
		DocumentContext jsonContext = JsonPath.parse(respBody);
		String createdEventId	 = jsonContext.read(eventNameJsonPath);
		System.out.println("Created Event: "+createdEventId);
		
		DataUtil.updateData("new.EventID", createdEventId);
	}
	
	
	@Test(description = "get EventType Details",enabled=true,priority = 2)
	public void getEventTypeDetais() {
		Client client = Client.create();
		System.out.println("ID: "+DataUtil.readData("new.EventID"));
		WebResource uri = client.resource(DataUtil.readData("BASE_URI")).path("/v1/event-types/"+DataUtil.readData("new.EventID"));
		
		ClientResponse resp = uri.header("Content-Type", "application/vnd.api+json")
				.header("Authorization", CommonApi.generateToken())
				.get(ClientResponse.class);
		
		String respBody = resp.getEntity(String.class);
		System.out.println("*****Response Body*****\n"+DataUtil.toPrettyFormat(respBody));
		
		//verifications
		Assert.assertEquals(resp.getStatus(), 200);
		
		DocumentContext jsonContext = JsonPath.parse(respBody);
		String eventNameJsonPath = "$.data.id";
		String createdEventID = jsonContext.read(eventNameJsonPath);
//		Assert.assertEquals(createdEventID, DataUtil.readData("new.EventID"));
	}
	
	@Test(description = "delete Event Type",enabled=true,priority = 3)
	public void deleteEventType() {
		Client client = Client.create();
		WebResource uri = client.resource(DataUtil.readData("BASE_URI")).path("/v1/event-types/"+DataUtil.readData("new.EventID"));
		
		ClientResponse resp = uri.header("Content-Type", "application/vnd.api+json")
								.header("Authorization", CommonApi.generateToken())
								.delete(ClientResponse.class);
		String respBody = resp.getEntity(String.class);
		System.out.println("*****Response Body*****\n"+DataUtil.toPrettyFormat(respBody));
		
		CommonApi.verifyDeleteResponse(resp,respBody);
	}
	
	@AfterMethod
	public void afterMethod(ITestResult result) {
		if(result.isSuccess()) {
			System.out.println("method name:" + result.getMethod().getMethodName()+" is Passed");
		}else {
			System.out.println("method name:" + result.getMethod().getMethodName()+" is Failed");
		}
	}
}
