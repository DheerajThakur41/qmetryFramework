import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import utils.DataUtil;

public class EventTypeApi extends TestBase {

	@Test(description="List All Event Locations",groups = {"Event Type"},priority = 0)
	public void listofAllEventTopic() {

		Response resp = 
				given()
				.queryParam("sort", "name")
				.queryParam("page[size]", "10")
				.queryParam("page[number]", "2")
				.spec(requestSpec)
				.headers(commonheaders)
				.when()
				.get("/v1/event-locations");

//		DataUtil.showResponseBody(resp);

		if(resp.getStatusCode()==200) {
			List<String> eventNameList=resp.path("data.attributes.name");
			Assert.assertEquals(resp.getStatusCode(),200);
			assertThat(eventNameList.toString(), containsString("Infostretch"));
		}else {
			Assert.fail("Failed : HTTP error code : " + resp.getStatusCode());
		}
	}

	@Test(description ="List All Event Types",groups = {"Event Type"},priority = 1 )
	public void getEventTypeList() {
		Response resp = 
				given()
				.queryParam("sort", "name")
				.queryParam("page[size]", "10")
				.queryParam("page[number]", "2")
				.spec(requestSpec)
				.headers(commonheaders)
				.when()
				.get("/v1/event-types");

		//DataUtil.showResponseBody(resp);

		if(resp.getStatusCode()==200) {
			Assert.assertEquals(resp.getStatusCode(),200);
			JsonPath jsonpath = resp.jsonPath();
			List<String> eventNameList1=jsonpath.getList("data.attributes.name");
//			assertThat(eventNameList1.toString(), containsString("Sample"));// event Name ContainsInfostretchor not
		}else {
			Assert.fail("Failed : HTTP error code : " + resp.getStatusCode());
		}
	}

	@Test(description ="Create New Event Type",groups = {"Event Type"},priority = 2 )
	public void createNewEventType() {
		String createEventTypeBody = null;
		try {
			createEventTypeBody = DataUtil.createEventType();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Response resp = 
				given()
				.spec(requestSpec)
				.headers(commonheaders)
				.body(createEventTypeBody)
				.when()
				.post("/v1/event-types");

		//DataUtil.showResponseBody(resp);
		String eventId = resp.path("data.id");
		String resEventName = resp.path("data.attributes.name");
		DataUtil.updateData("Event.TypeId", eventId);
		Reporter.log("created Event ID:"+eventId,true);

		Assert.assertEquals(resp.getStatusCode(), 201);//Assert Status code
	}
	
	@Test(description ="get Event Type Detail",groups = {"Event Type"},priority = 3 )
	public void getEventTypeDetail() {

		Response resp = 
				given()
				.spec(requestSpec)
				.headers(commonheaders)
				.when()
				.get("/v1/event-types/"+DataUtil.getData("Event.TypeId"));

//		DataUtil.showResponseBody(resp);
		
		if(resp.getStatusCode()==200) {
			Assert.assertEquals(resp.path("data.id"),DataUtil.getData("Event.TypeId"));
			Assert.assertEquals(resp.path("data.attributes.name"),"Camp, Treat and Retreat");
		}else {
			Assert.fail("Failed : HTTP error code : " + resp.getStatusCode());
		}
	}
	
	@Test(description ="Update Event Type",groups = {"Event Type"},priority = 4 )
	public void updateEventType() {
		String updateEventData = null;
		try {
			updateEventData = DataUtil.getEventTypePatch();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Response resp = 
				given()
				.spec(requestSpec)
				.headers(commonheaders)
				.body(updateEventData)
				.when()
				.patch("/v1/event-types/"+DataUtil.getData("Event.TypeId"));

//		DataUtil.showResponseBody(resp);
		
		if(resp.getStatusCode()==200) {
			Assert.assertEquals(resp.path("data.id"),DataUtil.getData("Event.TypeId"));
			Assert.assertEquals(resp.path("data.attributes.name"),"Updated Camp, Treat and Retreat");
		}else {
			Assert.fail("Failed : HTTP error code : " + resp.getStatusCode());
		}
	}
	
	@Test(description ="Delete Event Type",groups = {"Event Type"},priority = 5 )
	public void deleteEvent() {
		Response resp = 
				given()
				.spec(requestSpec)
				.headers(commonheaders)
				.when()
				.delete("/v1/event-types/"+DataUtil.getData("Event.TypeId"));
		
//		DataUtil.showResponseBody(resp);
		if(resp.getStatusCode()==200) {
			Reporter.log("Deleted Event ID:"+DataUtil.getData("Event.TypeId"),true);
			Assert.assertEquals(resp.path("meta.message"),DataUtil.getData("delete.msg"));
		}else {
			Assert.fail("Failed : HTTP error code : " + resp.getStatusCode());
		}
	}
}
