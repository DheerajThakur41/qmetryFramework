import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;

import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class CommonApi {
	
	@Test
	public  static String generateToken() {
		String aceessToken = null;
		try {
			Client client = Client.create();
			WebResource uri = client.resource("http://qe.events.infostretch.com/api/v1/auth/login");
			
			JsonObject userDetails=new JsonObject();
			userDetails.addProperty("email","admin@mailinator.com");
			userDetails.addProperty("password","admin123#");
			userDetails.addProperty("remember-me",true);
			String payloadData=userDetails.toString();
			
//			String payloadData = "{\"email\":\"admin@mailinator.com\",\"password\":\"admin123#\"}";

			 ClientResponse response = uri.type("application/json").post(ClientResponse.class, payloadData);

//			System.out.println(response.toString());
			String respBody = response.getEntity(String.class);
			DocumentContext jsonContext = JsonPath.parse(respBody);

			 aceessToken ="JWT "+jsonContext.read("$.access_token");
			
			 	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aceessToken;
	}
	
	public static void verifyDeleteResponse(ClientResponse resp,String respBody) {
		//validation
		if(resp.getStatus()==404) {
			System.out.println("Object Already Deleted");
			Assert.fail("Object Already Deleted");
		}
		if(resp.getStatus()==200) {
			DocumentContext jsonContext = JsonPath.parse(respBody);
			String deleteMsgJsonPath = "$.meta.message";
			String deleteMsg = jsonContext.read(deleteMsgJsonPath);
			Assert.assertEquals("Object successfully deleted", deleteMsg);
		}		
		assertThat(resp.getHeaders(), hasEntry(equalTo("Content-Type"), Matchers.hasItem("application/vnd.api+json")));
	}
}
