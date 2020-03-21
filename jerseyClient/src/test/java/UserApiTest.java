import static org.testng.Assert.assertEquals;

import java.io.FileWriter;
import java.util.Random;

import org.json.simple.JSONObject;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import junit.framework.Assert;
import util.DataUtil;

public class UserApiTest {


	
	/**
	 * @desc created for create user with Json File
	 *
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void createUser() {
		Client client = Client.create(); WebResource uri =
				client.resource(DataUtil.readData("BASE_URI")).path("/v1/users");

		Object obj = null;
		try {
			obj = DataUtil.readJsonFile("UserPayload.json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		JSONObject jsonObject = (JSONObject) obj;
		JSONObject data = (JSONObject) jsonObject.get("data");
		JSONObject attributes = (JSONObject) data.get("attributes");
		String name = (String) attributes.get("name");//Take Name Value
		data.remove("id");
		attributes.put("password", "Test@12345");
		attributes.put("email", DataUtil.getRandomMail());
		attributes.put("details", "Dummy User");
		String userData=null;
		FileWriter file;
		try {
			file = DataUtil.writeJsonFile("UserPayload.json");
			file.write(jsonObject.toString());
			file.flush();
			userData = DataUtil.readJsonFile("UserPayload.json").toString();

			ClientResponse resp = uri.header("Content-Type","application/vnd.api+json")
					.header("Authorization",CommonApi.generateToken())
					.post(ClientResponse.class,userData);

			String respBody = resp.getEntity(String.class);
			System.out.println("*****Response Body*****\n"+DataUtil.toPrettyFormat(respBody));

			DocumentContext jsonContext = JsonPath.parse(respBody); 
			String tokenJsonPath= "$.data.id"; 
			String newUserId = jsonContext.read(tokenJsonPath);
			data.put("id", newUserId);
			FileWriter file2 = DataUtil.writeJsonFile("UserPayload.json");
			file2.write(jsonObject.toString());
			file2.flush();

			DataUtil.updateData("new.UserId", newUserId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void getUserDetails() {
		Client client = Client.create();
		WebResource uri = client.resource(DataUtil.readData("BASE_URI")).path("/v1/users/"+DataUtil.readData("new.UserId"));

		ClientResponse resp = uri.header("Content-Type", "application/vnd.api+json")
				.header("Authorization", CommonApi.generateToken())
				.get(ClientResponse.class);
		String respBody = resp.getEntity(String.class);
//		System.out.println("*****Response Body*****\n"+DataUtil.toPrettyFormat(respBody));
	}	

	@Test
	public void viewAllUser() {
		Client client = Client.create();
		WebResource uri = client.resource(DataUtil.readData("BASE_URI")).path("/v1/users");

		ClientResponse resp = uri.queryParam("sort","email")
				.header("Content-Type", "application/vnd.api+json")
				.header("Authorization", CommonApi.generateToken())
				.get(ClientResponse.class);
		String respBody = resp.getEntity(String.class);

//		System.out.println("*****Response Body*****\n"+DataUtil.toPrettyFormat(respBody));
	}	

	@Test
	public void deleteUser() {
		Client client = Client.create();
		WebResource uri = client.resource(DataUtil.readData("BASE_URI")).path("/v1/users/"+DataUtil.readData("new.UserId"));

		ClientResponse resp = uri.header("Content-Type", "application/vnd.api+json")
				.header("Authorization", CommonApi.generateToken())
				.delete(ClientResponse.class);
		String respBody = resp.getEntity(String.class);
//		System.out.println("*****Response Body*****\n"+DataUtil.toPrettyFormat(respBody));
		
		//Validation
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
