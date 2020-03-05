import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import io.restassured.response.Response;
import utils.DataUtil;

public class EventTopics extends TestBase {

	@Test(description="List All Event Topic",groups = {"Event Topic"},priority = 0)
	public void listofAllEventTopic() {

		Response resp = 
				given()
				.spec(requestSpec)
				.headers(commonheaders)
				.when()
				.get("/v1/event-topics");

		//		DataUtil.showResponseBody(resp);
		if(resp.getStatusCode()==200) {
			List<String> eventNameList=resp.path("data.attributes.name");
			Assert.assertEquals(resp.getStatusCode(),200);
			assertThat(eventNameList.toString(), containsString("Travel & Outdoor"));
		}else {
			Assert.fail("Failed : HTTP error code : " + resp.getStatusCode());
		}
	}

	@Test(description ="Create New Event Topic",groups = {"Event Topic"},priority = 1 )
	public void createNewTopic() {
		String createEventTopicBody = null;
		try {
			createEventTopicBody = DataUtil.createEventTopicBody();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Response resp = 
				given()
				.spec(requestSpec)
				.headers(commonheaders)
				.body(createEventTopicBody)
				.when()
				.post("/v1/event-topics");

		//DataUtil.showResponseBody(resp);
		String eventTopicId = resp.path("data.id");
		String EventTopicName = resp.path("data.attributes.name");
		DataUtil.updateData("Event.TopicId", eventTopicId);
		Reporter.log("created Event ID:"+eventTopicId+"/nEvent Topic Name"+EventTopicName,true);

		Assert.assertEquals(resp.getStatusCode(), 201);//Assert Status code
	}

	@Test(description ="Get Event Topic Details",groups = {"Event Topic"},priority = 2 )
	public void EventTopicDetail() {

		Response resp = 
				given()
				.spec(requestSpec)
				.headers(commonheaders)
				.when()
				.get("/v1/event-topics/"+DataUtil.getData("Event.TopicId"));

		//		DataUtil.showResponseBody(resp);

		if(resp.getStatusCode()==200) {
			Assert.assertEquals(resp.path("data.id"),DataUtil.getData("Event.TopicId"));
			Assert.assertEquals(resp.path("data.attributes.name"),"Travel & Outdoor");
		}else {
			Assert.fail("Failed : HTTP error code : " + resp.getStatusCode());
		}
	}

	@Test(description ="Delete Event Topic",groups = {"Event Topic"},priority = 4 )
	public void DeleteEventTopic() {
		Response resp = 
				given()
				.spec(requestSpec)
				.headers(commonheaders)
				.when()
				.delete("/v1/event-topics/"+DataUtil.getData("Event.TopicId"));

		//		DataUtil.showResponseBody(resp);
		
		if(resp.getStatusCode()==200) {
			Reporter.log("Deleted Event ID:"+DataUtil.getData("Event.TopicId"),true);
			Assert.assertEquals(resp.path("meta.message"),DataUtil.getData("delete.msg"));
		}else {
			Assert.fail("Failed : HTTP error code : " + resp.getStatusCode());
		}
	}
	
	@Test(description ="Update Event Topic",groups = {"Event Topic"},priority = 3 )
	public void UpdateEventTopic() {
	
		String patchEventTopicBody = null;
		try {
			patchEventTopicBody = DataUtil.patchEventTopicBody();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Response resp = 
				given()
				.spec(requestSpec)
				.headers(commonheaders).body(patchEventTopicBody)
				.when()
				.patch("/v1/event-topics/"+DataUtil.getData("Event.TopicId"));

		//		DataUtil.showResponseBody(resp);

		if(resp.getStatusCode()==200) {
			Assert.assertEquals(resp.path("data.id"),DataUtil.getData("Event.TopicId"));
			Assert.assertEquals(resp.path("data.attributes.name"),"Updated Travel & Outdoor");
		}else {
			Assert.fail("Failed : HTTP error code : " + resp.getStatusCode());
		}
	
	}
	
}

