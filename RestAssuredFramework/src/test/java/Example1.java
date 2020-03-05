//io.restassured.matcher.RestAssuredMatchers.*
import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.hasItems;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

public class Example1 {
	
	@Test(description = "checks winner ID and loto ID",enabled = false)
	public void testFirst() {
//		get("http://localhost:3000/lotto").then().body("lotto.lottoId", equalTo(5));
		get("http://localhost:3000/lotto").then().body("winners.winnerId", hasItems(23, 54));
//		get("/lotto").then().log().body(); //check the body i response
	}
	
	
	
	@Test(description = "Test Json VAlidator",enabled = true)
	public void jsonSchemeValidator() throws IOException {
//		InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream("products-schema.json");
//		String jsonDataSourceString = new Scanner(jsonInputStream, "UTF-8").useDelimiter("\\Z").next();
		 String schema = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("products-schema.json"));
//		System.out.println(schema);
		get("/title").then().log().body();
//		get("/title").then().assertThat().body(JsonSchemaValidator.matchesJsonSchema(schema));
	}
	
	
	
	
	
}
