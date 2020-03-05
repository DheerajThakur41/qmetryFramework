import static io.restassured.RestAssured.given;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import io.restassured.response.Response;
import utils.DataUtil;

public class Event extends TestBase {

	@Test(description = "List All Event",priority = 1,enabled=true)
	public void ListOfAllEvent() {
	
		Response resp = 
				given()
				.spec(requestSpec)
				.headers(commonheaders)
				.when()
				.get("/v1/events");
	
//		DataUtil.showResponseBody(resp);
		
		if(resp.getStatusCode()==200) {
			List<String> eventNameList=resp.path("data.attributes.name");
			Assert.assertEquals(resp.getStatusCode(),200);
			assertThat(eventNameList.toString(), containsString("Event"));
		}else {
			Assert.fail("Failed : HTTP error code : " + resp.getStatusCode());
		}
	}
	
	@Test(description ="Create New Event",groups = {"Event"},priority = 2 )
	public void createNewEvent() {
		String createEventBody = null;
		try {
			createEventBody = DataUtil.createEventBody();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Response resp = 
				given()
				.spec(requestSpec)
				.headers(commonheaders)
				.body(createEventBody)
				.when()
				.post("/v1/events");

		//DataUtil.showResponseBody(resp);
		String eventId = resp.path("data.id");
		String resEventName = resp.path("data.attributes.name");
		DataUtil.updateData("Event.Id", eventId);
		Reporter.log("created Event ID:"+eventId,true);

		Assert.assertEquals(resp.getStatusCode(), 201);//Assert Status code
	}
	
	@Test(description ="Delete Event",groups = {"Event"},priority = 5 )
	public void DeleteEvent() {
		Response resp = 
				given()
				.spec(requestSpec)
				.headers(commonheaders)
				.when()
				.delete("/v1/events/"+DataUtil.getData("Event.Id"));

		//		DataUtil.showResponseBody(resp);
		
		if(resp.getStatusCode()==200) {
			Reporter.log("Deleted Event ID:"+DataUtil.getData("Event.TopicId"),true);
			Assert.assertEquals(resp.path("meta.message"),DataUtil.getData("delete.msg"));
		}else {
			Assert.fail("Failed : HTTP error code : " + resp.getStatusCode());
		}
	}
	
	@Test(description ="Update Event",groups = {"Event Type"},priority = 4 )
	public void updateEvent() {
		String updateEventData = null;
		try {
			updateEventData = DataUtil.patchEventBody();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Response resp = 
				given()
				.spec(requestSpec)
				.headers(commonheaders)
				.body(updateEventData)
				.when()
				.patch("/v1/events/"+DataUtil.getData("Event.Id"));

//		DataUtil.showResponseBody(resp);
		
		if(resp.getStatusCode()==200) {
			Assert.assertEquals(resp.path("data.id"),DataUtil.getData("Event.Id"));
			Assert.assertEquals(resp.path("data.attributes.name"),DataUtil.getData("Updated.Event.Name"));
		}else {
			Assert.fail("Failed : HTTP error code : " + resp.getStatusCode());
		}
	}

	@Test(description ="Get Event Topic Details",groups = {"Event Topic"},priority = 3 )
	public void EventDetail() {

		Response resp = 
				given()
				.spec(requestSpec)
				.headers(commonheaders)
				.when()
				.get("/v1/events/"+DataUtil.getData("Event.Id"));

		//		DataUtil.showResponseBody(resp);

		if(resp.getStatusCode()==200) {
			Assert.assertEquals(resp.path("data.id"),DataUtil.getData("Event.Id"));
			Assert.assertEquals(resp.path("data.attributes.name"),"Locator Approaches Best Practice");
		}else {
			Assert.fail("Failed : HTTP error code : " + resp.getStatusCode());
		}
	}
}
