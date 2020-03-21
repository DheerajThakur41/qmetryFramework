package com.qaf.api.test;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.qaf.api.utils.DataUtil;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.step.WsStep;
import com.qmetry.qaf.automation.ws.rest.RestTestBase;

import net.minidev.json.JSONObject;

public class TestAuthApi {

	Map<String,Object> data=new HashMap<String,Object>();
	String token = DataUtil.getData("admin.token");
	
	@Test(description = "Test Access token Api", enabled = true)
	public void setAccessToken() {
		WsStep.userRequests("admin.login.token");

		WsStep.responseShouldHaveStatusCode(200);
		WsStep.responseShouldHaveJsonPath("access_token");
		String messageBody = new RestTestBase().getResponse().getMessageBody();
		System.out.println("Message Body: " + JsonPath.read(messageBody, "$.access_token"));
		
	}

	@Test(description = "Test sucessfull Logout", enabled = false)
	public void logoutuser() {
		data.put("token", token);

		WsStep.userRequests("logout.user", data);
		WsStep.responseShouldHaveStatusCode(200);
		WsStep.responseShouldHaveValueIgnoringCase("true", "success");
	}

	@Test(description = "Test Remember me token Api", enabled = true)
	public void authRememberme() {
		data.put("token", token);

		WsStep.userRequests("admin.remember.token", data);

		WsStep.responseShouldHaveStatusCode(200);
		WsStep.responseShouldHaveJsonPath("access_token");

		String messageBody = new RestTestBase().getResponse().getMessageBody();
		DocumentContext jsonContext = JsonPath.parse(messageBody);

		String tokenJsonPath = "$.access_token";
		String aceessToken = jsonContext.read(tokenJsonPath);
		System.out.println("our aceessToken :" + aceessToken);

		DataUtil.updateData("admin.token", aceessToken);
	}

	@Test(description = "Test for mobile client Authorization", enabled = true)
	public void authForMobileClient() {

		WsStep.userRequests("mobileClient.login.token");
		WsStep.responseShouldHaveStatusCode(200);
		WsStep.responseShouldHaveJsonPath("access_token");
		
	}
	
	


}
