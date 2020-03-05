import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class ApiTestSol {

	credentials admincredential=new credentials();
	AuthenticationToken authenticationToken;
	RequestSpecification requestSpec;
	 static ResponseSpecification respspec;
	
	 //for user 
	 String newUser_email="rahul@app.com";
	 String newUSer_Password="password1234";
	 String newUSer_firstName="Rahulj";
	 String newUSer_Updated_firstName="raman";
	 String newUSer_id="";
	 String newEvent_name="quaterly, Treat Retreat";
	 String eventType="event-type";//case Sensetive
	 String eventId="";
	 String eventDelete_msg="Object successfully deleted";
	 String updateEvent_name="annualy, Treat Retreat";
	 
	 HashMap<String, String> commonheaders = new HashMap<String, String>();
	 
	 @BeforeClass
	public void setup() {
//		RestAssured.baseURI="http://qe.events.infostretch.com/api/";
		requestSpec = new RequestSpecBuilder().setBaseUri(URI.create("http://qe.events.infostretch.com/api/")).build();
		
		admincredential.setUsername("admin@mailinator.com");
		admincredential.setPassword("admin123#");
	}
	
	 @Test(description="get Access token from response",enabled = true,groups ={"User","Event"})
	public void setAuthtenticationtoken() {
		
		 authenticationToken =given().spec(requestSpec).
				contentType("application/json").
				body(admincredential).expect().statusCode(200).
					when().
					post("v1/auth/login").
					then().log().all().extract().as(AuthenticationToken.class);
		 
		//use for other requests 
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/vnd.api+json");
		headers.put("Authorization", "JWT "+authenticationToken.getToken());
		this.commonheaders=headers;
		
		//	String Token=resp.path("access_token");
		Reporter.log("Auth Token:>"+authenticationToken.getToken(),true);
	}
	
	 @Test(description="Get list of all user",enabled = false,groups ={"Event"},dependsOnMethods = {"setAuthtenticationtocken"})
	public void listofAllUser() {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/vnd.api+json");
		headers.put("Authorization", "JWT "+authenticationToken.getToken());
		
		Response resp = given()
			.spec(requestSpec)
			.headers(headers)
			.queryParam("sort", "email")
			.expect().statusCode(200)
		.when()
			.get("/v1/users");
		
		Assert.assertEquals(resp.getStatusCode(),200);
		
	}
	
	@Test(description="create a new user",enabled = true,dependsOnMethods = {"listofAllUser"},groups={"User"})
	public void createUser() {
			
//		String jsonreq = "{\"data\":{\"attributes\":{\"email\":\"tamm@tttt.com\",\"password\":\"password143\",\"name\":\"tamm\"},\"type\":\"user\"}}";
		
		// defining payload body data
		JsonObject payload = new JsonObject();
		JsonObject data = new JsonObject();
		
		JsonObject attribute = new JsonObject();
		attribute.addProperty("email", newUser_email);
		attribute.addProperty("password", newUSer_Password);
		attribute.addProperty("first-name", newUSer_firstName);
		
		data.add("attributes", attribute);
		data.addProperty("type", "user");
		payload.add("data", data);
		String payloaddata=payload.toString();
		
		System.out.println("MAp:"+commonheaders.toString());
		 Response resp = 
				 given()
				 	.spec(requestSpec)
				 	.headers(commonheaders)
				 	.body(payloaddata).
				 when()
				 	.post("/v1/users");
		
		 //Print Response Body
		 String responseBody = resp.getBody().asString();
		 System.out.println("Response Body"+responseBody);
		 
		 //get response code
		 int statusCode=resp.getStatusCode();
		 System.out.println("status code: "+statusCode);
		 Assert.assertEquals(statusCode, 201);
		
		 //get response line
		 System.out.println("status Line: "+resp.statusLine());
		 
		 
		 JsonPath jsonpath = resp.jsonPath();
		
		 newUSer_id=jsonpath.getString("data.id");
		 String email = jsonpath.getString("data.attributes.email");
		 String name =jsonpath.getString("data.attributes.first-name");
		 
		 Assert.assertEquals(email, newUser_email);//assert for email
		 Assert.assertEquals(newUSer_firstName, name);//assert for name
		 
		 System.out.println("ID:"+ newUSer_id+"\nEmail:"+email);
		 System.out.println("User First Name"+jsonpath.getString("data.attributes.first-name"));
//		 List<Integer> winnerIds = from(json).get("lotto.winners.winnerId");
	}
	
	@Test(description = "Update the user details",dependsOnMethods = {"createUser"},groups = {"User"})
	public void updateuserName() {
		
		// defining payload body data
				JsonObject payload = new JsonObject();
				JsonObject data = new JsonObject();		
				
				JsonObject attribute = new JsonObject();
				attribute.addProperty("email", newUser_email);
				attribute.addProperty("password", newUSer_Password);
				attribute.addProperty("first-name", newUSer_Updated_firstName);
				
				data.add("attributes", attribute);
				data.addProperty("type", "user");
				data.addProperty("id", newUSer_id);
				payload.add("data", data);
				String Payload=payload.toString();
		 Response resp =
				 
				 given()
				 	.spec(requestSpec)
				 	.headers(commonheaders)
				 	.body(Payload)
				 .when()
				 	.patch("/v1/users/"+newUSer_id);
		 
		 //Print Response Body
		 showResponseBody(resp);
		 
		 //get response code
		 int statusCode=resp.getStatusCode();
		 System.out.println("status code: "+statusCode);
		 Assert.assertEquals(statusCode, 200);
		
		 //get response line
		 System.out.println("status Line: "+resp.statusLine());
		 
		 JsonPath jsonpath = resp.jsonPath();
		 String USer_id=jsonpath.getString("data.id");
		 String name =jsonpath.getString("data.attributes.first-name");
		 String email = jsonpath.getString("data.attributes.email");
		 
		 Assert.assertEquals(USer_id, newUSer_id);//
		 Assert.assertEquals(email,newUser_email);//assert for email
		 Assert.assertEquals(name, newUSer_Updated_firstName);//assert for updated name
		 
	}
	
	@Test(description ="Delete The created user",dependsOnMethods = {"updateuserName"},groups = { "User" } )
	public void deleteUserName() {
		 Response resp = 
				 given()
//				 	.log().all()
				 	.spec(requestSpec)
				 	.headers(commonheaders)
				 .when()
				 	.delete("/v1/users/"+newUSer_id);
		 
		 //Print Response Body
		 showResponseBody(resp);
		 
		 //get response code
		 int statusCode=resp.getStatusCode();
		 System.out.println("status code: "+statusCode);
		 Assert.assertEquals(statusCode, 200);
		
		 //get response line
		 System.out.println("status Line: "+resp.statusLine());
		 
		 JsonPath jsonpath = resp.jsonPath();
		 String deleteMsg = jsonpath.getString("meta.message");
		 
		 //Assert message deleted successfully
		 Assert.assertEquals(deleteMsg,"Object successfully deleted");
	}
	
	@Test(description ="Event List",dependsOnMethods = {"setAuthtenticationtoken"},groups = {"Event"} )
	public void EventList() {
		Response resp = 
				 given()
				 	.queryParam("sort", "name")
				 	.queryParam("page[size]", "10")
				 	.queryParam("page[number]", "2")
				 	.spec(requestSpec)
				 	.headers(commonheaders)
				 .when()
				 	.get("/v1/event-locations");
		
		showResponseBody(resp);
		
		JsonPath jsonpath = resp.jsonPath();
		List<String> eventNameList=jsonpath.getList("data.attributes.name");
		 
//		List<String> winnerIds=resp.then().extract().path("data.[*].attributes.name");
		
		System.out.println(eventNameList.toString());
		
		Assert.assertEquals(resp.getStatusCode(), 200);//Assert Status code
		assertThat(eventNameList.toString(), containsString("infostretch"));// event Name ContainsInfostretchor not
	}
	
	@Test(description ="Event List",dependsOnMethods = {"setAuthtenticationtoken"},groups = {"Event"} )
	public void createNewEvent() {
		
		// defining payload body data
				JsonObject payload = new JsonObject();
				JsonObject data = new JsonObject();
				
				JsonObject attribute = new JsonObject();
				attribute.addProperty("name", newEvent_name);
				
				data.add("attributes", attribute);
				data.addProperty("type", eventType);
				payload.add("data", data);
				String payloaddata=payload.toString();
				
				System.out.println(payloaddata);
		Response resp = 
				 given()
				 	.spec(requestSpec)
				 	.headers(commonheaders)
				 	.body(payloaddata)
				 .when()
				 	.post("/v1/event-types");
		
		showResponseBody(resp);
		
		eventId = resp.then().extract().path("data.id");
		String resEventName = resp.then().extract().path("data.attributes.name");
		Reporter.log("Event ID:"+eventId,true);
		
		Assert.assertEquals(resEventName, newEvent_name);
		Assert.assertEquals(resp.getStatusCode(), 201);//Assert Status code
	}
	
	@Test(description ="Get Event Details",dependsOnMethods = {"createNewEvent"},groups = {"Event"} )
	public void getEventDetails() {
		
		Response resp = 
				 given()
				 	.spec(requestSpec)
				 	.headers(commonheaders)
				 .when()
				 	.get("/v1/event-types/"+eventId);
		
		showResponseBody(resp);
		
		JsonPath jsonpath = resp.jsonPath();
		String resEventName=jsonpath.getString("data.attributes.name");
		String respEventId=jsonpath.getString("data.id");
		
		Reporter.log("Reponse Event Name:"+resEventName,true);
		Reporter.log("Reponse Event Id:"+respEventId,true);
		
		Assert.assertEquals(resp.getStatusCode(), 200);//Assert Status code
		Assert.assertEquals(respEventId, eventId);
		Assert.assertEquals(resEventName, newEvent_name);
	}
	
	@Test(description ="Update Event Name with Patch",dependsOnMethods = {"getEventDetails"},groups = {"Event"} )
	public void updateEvents() {
		
		// defining payload body data
		JsonObject payload = new JsonObject();
		JsonObject data = new JsonObject();
		
		JsonObject attribute = new JsonObject();
		attribute.addProperty("name", updateEvent_name);
		
		data.add("attributes", attribute);
		data.addProperty("type", eventType);
		data.addProperty("id", eventId);
		payload.add("data", data);
		
		String payloadData=payload.toString();
		
		Response resp = 
				 given()
				 	.spec(requestSpec)
				 	.headers(commonheaders)
				 	.body(payloadData)
				 .when()
				 	.patch("/v1/event-types/"+eventId);
		
		showResponseBody(resp);
		
		JsonPath jsonpath = resp.jsonPath();
		String respEventId=jsonpath.getString("data.id");
		String respUpdatedEvent_name=jsonpath.getString("data.attributes.name");
		
		Reporter.log("Reponse Event Id:"+respEventId,true);
		Reporter.log("Reponse Event name:"+respUpdatedEvent_name,true);
		
		Assert.assertEquals(resp.getStatusCode(), 200);//Assert Status code
		Assert.assertEquals(respUpdatedEvent_name, updateEvent_name);
		Assert.assertEquals(respEventId, eventId);
	}
	
	@Test(description ="Delete Event",dependsOnMethods = {"createNewEvent","updateEvents"},groups = {"Event"} )
	public void deleteEvents() {
		
		Response resp = 
				 given()
				 	.spec(requestSpec)
				 	.headers(commonheaders)
				 .when()
				 	.delete("/v1/event-types/"+eventId);
		
		showResponseBody(resp);
		
		JsonPath jsonpath = resp.jsonPath();
		String respDeleteMsg=jsonpath.getString("meta.message");
		
		Reporter.log("Reponse message:"+respDeleteMsg,true);
		
		Assert.assertEquals(resp.getStatusCode(), 200);//Assert Status code
		Assert.assertEquals(respDeleteMsg, eventDelete_msg);
	}
	
	@Test(description ="Get a list of Event Topics",dependsOnMethods = {"setAuthtenticationtoken"},groups = {"Event Topic"} )
	public void listofEventTopics() {
		
		Response resp = 
				 given()
				 	.spec(requestSpec).queryParam("sort", "name")
				 	.headers(commonheaders)
				 .when()
				 	.get("/v1/event-topics");
		
		showResponseBody(resp);
		
		JsonPath jsonpath = resp.jsonPath();
		String respEventTopicName=jsonpath.getString("data.attributes.name");
		
		Reporter.log("Reponse message:"+respEventTopicName,true);
		
		Assert.assertEquals(resp.getStatusCode(), 200);//Assert Status code
	}
	
	
	
	
//	@BeforeClass
//	public static void responsecheck() {
//		respspec = new ResponseSpecBuilder().expectContentType(ContentType.JSON).expectStatusCode(200).build();
//	}
	
	public void showResponseBody(Response resp) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(resp.getBody().asString());
		String prettyJsonString = gson.toJson(je);
		System.out.println("===========Response Body==================\n"+prettyJsonString);
	}
	
	public static void main(String[] args) {
//		
	}
	
}
