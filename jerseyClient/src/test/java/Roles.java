import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;

import java.io.FileWriter;

import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
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

public class Roles {

	
	@Test(description = "List Roles",enabled=true, priority = -1)
	public void getListofRoles() {
		Client client = Client.create();
		WebResource uri = client.resource(DataUtil.readData("BASE_URI")).path("/v1/roles");
		
		ClientResponse resp = uri.header("Content-Type", "application/vnd.api+json")
				.header("Authorization", CommonApi.generateToken())
				.get(ClientResponse.class);
		
		String respBody = resp.getEntity(String.class);
//		System.out.println("*****Response Body*****\n"+DataUtil.toPrettyFormat(respBody));
		
		DocumentContext jsonContext = JsonPath.parse(respBody);
		
		if(resp.getStatus()==200) {
			Assert.assertEquals(resp.getStatus(),200);
			String numberOfRoleJsonPath = "$.data.length()";
			Integer numberOfRole = jsonContext.read(numberOfRoleJsonPath);
			System.out.println("Number of Roles: "+numberOfRole);
			assertThat(resp.getHeaders(), hasEntry(equalTo("Content-Type"), Matchers.hasItem("application/vnd.api+json")));
		}else {
				Assert.fail("wrong Request");
		}
	}
	
	@Test(description = "create Roles",enabled=true, priority = 0)
	public void createRoles() {
		Client client = Client.create();
		WebResource uri = client.resource(DataUtil.readData("BASE_URI")).path("/v1/roles");
		
		Object obj = null;
		try {
			obj = DataUtil.readJsonFile("roles.json");

		JSONObject jsonObject = (JSONObject) obj;
		JSONObject data = (JSONObject) jsonObject.get("data");
		JSONObject attributes = (JSONObject) data.get("attributes");
		attributes.put("name", DataUtil.getRandomEvent());
		attributes.put("title-name",  DataUtil.getRandomEvent());
		String userData=null;
		FileWriter file;
			file = DataUtil.writeJsonFile("roles.json");
			file.write(jsonObject.toString());
			file.flush();
			userData = DataUtil.readJsonFile("roles.json").toString();
			
		ClientResponse resp = uri.header("Content-Type", "application/vnd.api+json")
				.header("Authorization", CommonApi.generateToken())
				.post(ClientResponse.class,userData);
		
		
		String respBody = resp.getEntity(String.class);
//		System.out.println("*****Response Body*****\n"+DataUtil.toPrettyFormat(respBody));
		
		Assert.assertEquals(resp.getStatus(), 201);
		DocumentContext jsonContext = JsonPath.parse(respBody); 
		String roleIdJsonPath= "$.data.id"; 
		String roleId = jsonContext.read(roleIdJsonPath);
		DataUtil.updateData("new.roleId", roleId);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test(description = "delete Roles",enabled=true, priority = 1)
	public void getRoleDetails() {
		Client client = Client.create();
		WebResource uri = client.resource(DataUtil.readData("BASE_URI")).path("/v1/roles/"+DataUtil.readData("new.roleId"));
		
		ClientResponse resp = uri.header("Content-Type", "application/vnd.api+json")
				.header("Authorization", CommonApi.generateToken())
				.get(ClientResponse.class);
		String respBody = resp.getEntity(String.class);
//		System.out.println("*****Response Body*****\n"+DataUtil.toPrettyFormat(respBody));
		
		//Validation
		Assert.assertEquals(resp.getStatus(), 200);
	}	
	
	@Test(description = "delete Roles",enabled=true, priority = 3)
	public void deleteRole() {
		Client client = Client.create();
		WebResource uri = client.resource(DataUtil.readData("BASE_URI")).path("/v1/roles/"+DataUtil.readData("new.roleId"));
		
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
