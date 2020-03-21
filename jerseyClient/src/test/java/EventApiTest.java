import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.FileWriter;
import java.util.List;

import org.json.simple.JSONObject;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import junit.framework.Assert;
import util.DataUtil;

public class EventApiTest {
	
	String eventID=null;
	
	@Test(description = "List All Event",priority = 1,enabled=true)
	public void ListOfAllEvent() {
		Client client = Client.create();
		WebResource uri = client.resource(DataUtil.readData("BASE_URI")).path("/v1/events");
		
		ClientResponse resp = uri.queryParam("sort","identifier")
								.header("Content-Type", "application/vnd.api+json")
								.header("Authorization", CommonApi.generateToken())
								.get(ClientResponse.class);
		String respBody = resp.getEntity(String.class);
		
		DocumentContext jsonContext = JsonPath.parse(respBody); 
		String eventNameJsonPath = "$.data[*].attributes.name"; 
		String eventIDJsonPath = "$.data[*].id"; 
		List<String> eventNameList =jsonContext.read(eventNameJsonPath); 
		List<String> eventIdList =jsonContext.read(eventIDJsonPath); 
		
		assertThat(eventNameList.toString(),containsString("example"));
		  
		System.out.println("####List of Event Name#####"); 
		  for (String eventName :eventNameList) 
		  { 
			  System.out.println("Name: "+eventName); 
		  }
//		System.out.println("*****Response Body*****\n"+DataUtil.toPrettyFormat(respBody));
	}	
	
	@SuppressWarnings("unchecked")
	@Test(description = "create An Event",priority = 2,enabled=true)
	public void createEvent() {
		Client client = Client.create();
		WebResource uri = client.resource(DataUtil.readData("BASE_URI")).path("/v1/events");
		String createEventData=null;
		try {
			Object obj=DataUtil.readJsonFile("createEvent.json");
			
			JSONObject jsonObject = (JSONObject) obj;
			JSONObject data = (JSONObject) jsonObject.get("data");
			JSONObject attributes = (JSONObject) data.get("attributes");
//			String name = (String) attributes.get("name");//Take Name Value
			attributes.put("starts-at", DataUtil.getNextDate(2));
			attributes.put("ends-at", DataUtil.getNextDate(4));
			attributes.put("schedule-published-on", DataUtil.getNextDate(-2));
			data.remove("id");
			
			FileWriter file = DataUtil.writeJsonFile("createEvent.json");
			file.write(jsonObject.toString());
			file.flush();
			
			createEventData=DataUtil.readJsonFile("createEvent.json").toString();
			ClientResponse resp = uri.queryParam("sort","identifier").header("Content-Type","application/vnd.api+json").header("Authorization",CommonApi. generateToken()) 
									.post(ClientResponse.class,createEventData);
					  
			String respBody = resp.getEntity(String.class);
			System.out.println("*****Response Body*****\n"+DataUtil.toPrettyFormat(respBody));
				
			DocumentContext jsonContext = JsonPath.parse(respBody); 
			String eventIDJsonPath = "$.data.id"; 
			String identifierJsonPath = "$.data.attributes.identifier"; 
			String identifierID =jsonContext.read(identifierJsonPath);
			String eventID =jsonContext.read(eventIDJsonPath);
			System.out.println("Event Created"+eventID);
			
			data.put("id", eventID);
			DataUtil.updateData("created.Event", eventID);	
			DataUtil.updateData("newEvent.identifier", identifierID);	
			
			FileWriter file2 = DataUtil.writeJsonFile("createEvent.json");
			file2.write(jsonObject.toString());
			file2.flush();
			
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(description = "Delete Event",priority = 4,enabled=true)
	public void deleteEvent() {
		Client client = Client.create();
		WebResource uri = client.resource(DataUtil.readData("BASE_URI")).path("/v1/events/"+DataUtil.readData("created.Event"));
		
		ClientResponse resp = uri.header("Content-Type", "application/vnd.api+json")
							.header("Authorization", CommonApi.generateToken())
							.delete(ClientResponse.class);
		
		String respBody = resp.getEntity(String.class);
		System.out.println("*****Response Body*****\n"+DataUtil.toPrettyFormat(respBody));
		
		//validation
		CommonApi.verifyDeleteResponse(resp,respBody);
	}
	
	@Test(description = "Get Event Details",priority = 3,enabled=true)
	public void getEvent() {
		Client client = Client.create();
		WebResource uri = client.resource(DataUtil.readData("BASE_URI")).path("/v1/events/"+DataUtil.readData("newEvent.identifier"));
		
		ClientResponse resp = uri.header("Content-Type", "application/vnd.api+json")
							.header("Authorization", CommonApi.generateToken())
							.get(ClientResponse.class);
		
		String respBody = resp.getEntity(String.class);
		System.out.println("*****Response Body*****\n"+DataUtil.toPrettyFormat(respBody));
		DocumentContext jsonContext = JsonPath.parse(respBody); 
		String eventIDJsonPath = "$.data.id";
		
		Assert.assertEquals(DataUtil.readData("created.Event"), jsonContext.read(eventIDJsonPath));
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
	

