import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;

import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import util.DataUtil;

public class AuthenticationApi {
	
	@Test
	public void logoutApi() {
		Client client = Client.create();
		WebResource uri = client.resource(DataUtil.readData("BASE_URI")).path("/v1/auth/logout");
		ClientResponse resp = uri.header("Content-Type", "application/json").post(ClientResponse.class);
		
		String respBody = resp.getEntity(String.class);
		System.out.println("*****Response Body*****\n"+DataUtil.toPrettyFormat(respBody));
		
		DocumentContext jsonContext = JsonPath.parse(respBody);
		String logoutMsgJsnpath = "$.success";
		boolean msg = jsonContext.read(logoutMsgJsnpath);
		
		//verifications
		Assert.assertEquals(resp.getStatus(), 200);
		Assert.assertEquals(msg, true);
		assertThat(resp.getHeaders(), hasEntry(equalTo("Content-Type"), Matchers.hasItem("application/json")));
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
