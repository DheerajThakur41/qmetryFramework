import static io.restassured.RestAssured.given;

import java.net.URI;
import java.util.HashMap;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import utils.DataUtil;

public class TestBase {

	static RequestSpecification requestSpec;
	static AuthenticationToken authenticationToken;
	static HashMap<String, String> commonheaders = new HashMap<String, String>();

	@BeforeTest
	public void setup() {
//			RestAssured.baseURI="http://qe.events.infostretch.com/api/";
		requestSpec = new RequestSpecBuilder().setBaseUri(URI.create(DataUtil.getData("BASE_URI"))).build();
		
		commonheaders.put("Content-Type", "application/vnd.api+json");
		commonheaders.put("Authorization", TestBase.getAuthtenticationtoken());
		// use for other requests
				
	}

	public static String getAuthtenticationtoken() {
		credentials admincredential = new credentials();
		admincredential.setUsername("admin@mailinator.com");
		admincredential.setPassword("admin123#");

		authenticationToken = given().spec(requestSpec).contentType("application/json").body(admincredential).expect()
				.statusCode(200).when().post("v1/auth/login").then().extract()
				.as(AuthenticationToken.class);

		// String Token=resp.path("access_token");
		
		
//		System.out.println("Auth Token:>" + authenticationToken.getToken());
			return "JWT "+authenticationToken.getToken();
	}

	@AfterMethod
	public void tearDown(ITestResult result) {
		if(result.isSuccess()) {
			System.out.println("method name:" + result.getMethod().getMethodName()+" is Passed");
		}else {
			System.out.println("method name:" + result.getMethod().getMethodName()+" is Failed");
		}
	}
}
