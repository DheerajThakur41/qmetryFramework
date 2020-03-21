import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;

import java.util.List;

import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import util.DataUtil;

public class EventTopicApi {	

	
	@Test(description = "get List of Event Topic",enabled=true, priority = 0)
	public void getListofEventTopic() {
		Client client = Client.create();
		WebResource uri = client.resource(DataUtil.readData("BASE_URI")).path("/v1/event-topics");
		
		ClientResponse resp = uri.queryParam("sort","name")
								.header("Content-Type", "application/vnd.api+json")
								.header("Authorization", CommonApi.generateToken())
								.get(ClientResponse.class);
		
		String respBody = resp.getEntity(String.class);
		
		//Validation
		Assert.assertEquals(resp.getStatus(), 200);
		DocumentContext jsonContext = JsonPath.parse(respBody);
		String eventNameJsonPath = "$.data[*].attributes.name";
		List<String> eventTopicNameList = jsonContext.read(eventNameJsonPath);
//		assertThat(eventTopicNameList.toString(), containsString("Carol Landing"));

		for (String eventTopicName : eventTopicNameList) {
			System.out.println("Name: "+eventTopicName);
		}
		System.out.println("*****Response Body*****\n"+DataUtil.toPrettyFormat(respBody));
	}	
	
	@Test(description = "create Event Topic",enabled=true, priority = 1)
	public void createEventTopic() {
		
		Client client = Client.create();
		WebResource uri = client.resource(DataUtil.readData("BASE_URI")).path("/v1/event-topics");
		
		String createEventData = null;
		try {
			createEventData = DataUtil.readJsonFile("eventTopic.json").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("createEventData "+createEventData);
		ClientResponse resp = uri.queryParam("sort","name")
								.header("Content-Type", "application/vnd.api+json")
								.header("Authorization", CommonApi.generateToken())
								.post(ClientResponse.class,createEventData);
		
		String respBody = resp.getEntity(String.class);
		System.out.println("*****Response Body*****\n"+DataUtil.toPrettyFormat(respBody));
		
		//verifications
		Assert.assertEquals(resp.getStatus(), 201);
		String eventNameJsonPath = "$.data.id";
		DocumentContext jsonContext = JsonPath.parse(respBody);
		String createdEventTopicId = jsonContext.read(eventNameJsonPath);
		System.out.println("Created Event Topic ID: "+createdEventTopicId);
		
		DataUtil.updateData("new.eventTopicID", createdEventTopicId);//set VAlue in File
		assertThat(resp.getHeaders(), hasEntry(equalTo("Content-Type"), Matchers.hasItem("application/vnd.api+json")));
	}
	
	@Test(description = "Get Event Topic Details",enabled=true, priority = 2)
	public void getEventTopic() {
		Client client = Client.create();
		WebResource uri = client.resource(DataUtil.readData("BASE_URI")).path("/v1/event-topics/"+DataUtil.readData("new.eventTopicID"));

		ClientResponse resp = uri.header("Content-Type", "application/vnd.api+json")
				.header("Authorization", CommonApi.generateToken())
				.get(ClientResponse.class);
		String respBody = resp.getEntity(String.class);
		System.out.println("*****Response Body*****\n"+DataUtil.toPrettyFormat(respBody));

		//validation
		if(resp.getStatus()==200) {
			Assert.assertEquals(resp.getStatus(),200);
			DocumentContext jsonContext = JsonPath.parse(respBody);
			String EventTopicIDJsonPath = "$.data.id";
			String eventTopicID = jsonContext.read(EventTopicIDJsonPath);
			System.out.println("Event Topic ID"+eventTopicID);
			assertThat(resp.getHeaders(), hasEntry(equalTo("Content-Type"), Matchers.hasItem("application/vnd.api+json")));
		}
		else {
			Assert.fail("Object not found");
		}
	}
	
	@Test(description = "Delete Event Topic Details",enabled=true, priority = 3)
	public void deleteEventTopic() {
		Client client = Client.create();
		WebResource uri = client.resource(DataUtil.readData("BASE_URI")).path("/v1/event-topics/"+DataUtil.readData("new.eventTopicID"));
		
		ClientResponse resp = uri.header("Content-Type", "application/vnd.api+json")
								.header("Authorization", CommonApi.generateToken())
								.delete(ClientResponse.class);
		String respBody = resp.getEntity(String.class);
		System.out.println("*****Response Body*****\n"+DataUtil.toPrettyFormat(respBody));
		
		//Validation
		CommonApi.verifyDeleteResponse(resp,respBody);
	}
	
	@Test(description = "Event Topic Details of an Event",enabled=false, priority =2)
	public void getEventTopicDetails() {
		Client client = Client.create();
		WebResource uri = client.resource(DataUtil.readData("BASE_URI")).path("/v1/event-topics/"+DataUtil.readData("new.eventTopicID")+"/event-topic");
		
		System.out.println("URI: "+uri.toString());
		ClientResponse resp = uri.header("Content-Type", "application/vnd.api+json")
				.header("Authorization", CommonApi.generateToken())
				.get(ClientResponse.class);
		String respBody = resp.getEntity(String.class);
		System.out.println("*****Response Body*****\n"+DataUtil.toPrettyFormat(respBody));

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
