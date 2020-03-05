import java.util.HashMap;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class CreateUser {

	@Test
	public void RegistrationSuccessful()
	{		
		RestAssured.baseURI ="http://qe.events.infostretch.com/api/";
		RequestSpecification request = RestAssured.given();

		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Accept", "application/vnd.api+json");
		headers.put("Authorization", "JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1NzkxNTY0NjQsIm5iZiI6MTU3OTE1NjQ2NCwianRpIjoiZGRlM2MzMTMtOWUzMC00ZTE0LWE4NGEtMzY5OGFlN2IwNTM2IiwiZXhwIjoxNTc5MTYxODY0LCJpZGVudGl0eSI6MiwiZnJlc2giOnRydWUsInR5cGUiOiJhY2Nlc3MiLCJjc3JmIjoiZjc1OGFkNDYtM2U5NC00YTY4LWFiNDAtM2I5NDQwMzNkMWVhIn0.j-ZHgCn6QSLpbPBKSzeqHH2sD-2zQMlgNMVYzr1usbY");
		
//		JSONObject requestParams = new JSONObject();
//		requestParams.put("FirstName", "Virender"); // Cast
//		requestParams.put("LastName", "Singh");
//		requestParams.put("UserName", "sdimpleuser2dd2011");
//		requestParams.put("Password", "password1");

//		request.headers(headers).body(requestParams.toJSONString());
		Response response = request.post("/v1/users");

		System.out.println(response.asString());
		int statusCode = response.getStatusCode();
//		Assert.assertEquals(statusCode, "201");
		String successCode = response.jsonPath().get("SuccessCode");
//		Assert.assertEquals( "Correct Success code was returned", successCode, "OPERATION_SUCCESS");
	}
	
	
}
