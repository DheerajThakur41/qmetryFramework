import static io.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.response.Response;
import utils.DataUtil;

public class UserApi extends TestBase{

	@Test(description="Get list of all user",enabled = true,priority = 0)
	public void listofAllUser() {

		Response resp = given()
				.spec(requestSpec)
				.headers(commonheaders)
				.queryParam("sort", "email")
				.expect().statusCode(200)
				.when()
				.get("/v1/users");

		Assert.assertEquals(resp.getStatusCode(),200);
	}

	@Test(description="create a new user",enabled = true,priority = 1)
	public void createUser() {
		String userData = null;
		try {
			userData = DataUtil.getCreateUserBody();
		} catch (Exception e) {
			System.out.println("Exception During file loading"+e.getMessage());
		}
		Response resp = 
				given()
				.spec(requestSpec)
				.headers(commonheaders)
				.body(userData).
				when()
				.post("/v1/users");
//		DataUtil.showResponseBody(resp);

		if(resp.getStatusCode()==201) {
			String newUserId=resp.path("data.id");
			DataUtil.updateData("new.UserId", newUserId);
			Assert.assertEquals(resp.path("data.attributes.email"),DataUtil.getData("User.email"));
			Assert.assertEquals(resp.path("data.attributes.first-name"),DataUtil.getData("User.Fname"));
		}
		else {
			Assert.fail("Failed : HTTP error code : " + resp.getStatusCode());
		}
	}

	@Test(description ="Delete The created user",enabled = true,priority = 3 )
	public void deleteUserName() {
		Response resp = 
				given()
				.spec(requestSpec)
				.headers(commonheaders)
				.when()
				.delete("/v1/users/"+DataUtil.getData("new.UserId"));

		//Print Response Body
//		DataUtil.showResponseBody(resp);

		if(resp.getStatusCode()==200) {
			Assert.assertEquals(resp.path("meta.message"),DataUtil.getData("delete.msg"));
		}else {
			Assert.fail("Failed : HTTP error code : " + resp.getStatusCode());
		}
	}

	@Test(description = "Update the user details",priority = 2)
	public void updateuserName() {
		String updatedUserData = null;
		try {
			updatedUserData = DataUtil.getupdatedUserBody();
		} catch (Exception e) {
			System.out.println("Exception During file loading"+e.getMessage());
		}

		Response resp = given()
				.spec(requestSpec)
				.headers(commonheaders)
				.body(updatedUserData)
				.when()
				.patch("/v1/users/"+DataUtil.getData("new.UserId"));

		//Print Response Body
//		DataUtil.showResponseBody(resp);

		if(resp.getStatusCode()==200) {
			Assert.assertEquals(resp.path("data.id"),DataUtil.getData("new.UserId"));
			Assert.assertEquals(resp.path("data.attributes.first-name"),DataUtil.getData("newUSer_firstName"));
			Assert.assertEquals(resp.path("data.attributes.email"),DataUtil.getData("newUser_email"));
		}else {
			Assert.fail("Failed : HTTP error code : " + resp.getStatusCode());
		}
	}
	
}
