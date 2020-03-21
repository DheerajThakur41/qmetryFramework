package com.qaf.api.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.qaf.api.utils.DataUtil;
import com.qmetry.qaf.automation.step.WsStep;
import com.qmetry.qaf.automation.ws.rest.RestTestBase;

public class TestUserApi extends BaseTest {

	Map<String,Object> data=new HashMap<String,Object>();
	
	
	@Test()
	public void listofallUser() {
		String token = DataUtil.getData("admin.token");
		data.put("token", token);
		
		
		WsStep.userRequests("list.user",data);
		WsStep.responseShouldHaveStatusCode(200);

		String messageBody = new RestTestBase().getResponse().getMessageBody();

		DocumentContext jsonContext = JsonPath.parse(messageBody);
		String firstNameJP = "data[*].attributes.first-name";
		String idJsonPath = "data[*].id";

		List<String> names = jsonContext.read(firstNameJP);
		List<String> ids = jsonContext.read(idJsonPath);

		for (int i = 0; i < names.size(); i++) {
			System.out.println("Name: " + names.get(i) + "\t Id: " + ids.get(i));
		}
	}

	@Test()
	public void createUser() {
		
		String token = DataUtil.getData("admin.token");
		String newMailId=DataUtil.randomString("ravi")+"@gmail.com";
		data.put("token", token);
		data.put("newUserMail_id", newMailId);
		
		WsStep.userRequests("create.user",data);
		String messageBody = new RestTestBase().getResponse().getMessageBody();
		
		WsStep.responseShouldHaveStatusCode(201); 
		DocumentContext jsonContext = JsonPath.parse(messageBody);
		String tokenJsonPath = "$.data.id";
		String newUserId = jsonContext.read(tokenJsonPath);

		System.out.println("New User ID :" + newUserId);
		DataUtil.updateData("new.UserId", newUserId);
	}

	@Test()
	public void getUserDetail() {
		String token = DataUtil.getData("admin.token");
		data.put("token", token);
		data.put("userId", DataUtil.getData("new.UserId"));
		WsStep.userRequests("get.userDetails", data);

		WsStep.responseShouldHaveValueIgnoringCase(DataUtil.getData("new.UserId"), "data.id");
	}

	@Test()
	public void deleteUser() {
		String token = DataUtil.getData("admin.token");
		data.put("token", token);
		data.put("userId", DataUtil.getData("new.UserId"));

		WsStep.userRequests("delete.user", data);
		WsStep.responseShouldHaveStatusCode(200);
		DataUtil.updateData("new.UserId", "");

		WsStep.responseShouldHaveValueIgnoringCase("Object successfully deleted", "meta.message");
	}
}
