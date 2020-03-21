package com.qaf.api.test;

import org.testng.annotations.BeforeTest;

import com.jayway.jsonpath.JsonPath;
import com.qaf.api.utils.DataUtil;
import com.qmetry.qaf.automation.step.WsStep;
import com.qmetry.qaf.automation.ws.rest.RestTestBase;

public class BaseTest {

	@BeforeTest
	public void getAccessToken() {
		WsStep.userRequests("admin.login.token");
		String messageBody = new RestTestBase().getResponse().getMessageBody();
		String token = "JWT " + JsonPath.read(messageBody, "$.access_token");
		DataUtil.updateData("admin.token", token); 
	}
	
	
}
