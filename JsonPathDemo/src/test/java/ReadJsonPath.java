
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import org.testng.annotations.Test;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

public class ReadJsonPath {
	public InputStream jsonInputStream = this.getClass().getClassLoader().getResourceAsStream("store.json");
	public String jsonDataSourceString = new Scanner(jsonInputStream, "UTF-8").useDelimiter("\\Z").next();
//	String filepath="D:\\MyPractice\\EclipseFolder\\JsonPathDemo\\src\\test\\resources\\sample-data.json";
//	new Scanner(new File(filepath)).useDelimiter("\\Z").next();
	DocumentContext jsonContext = JsonPath.parse(jsonDataSourceString);

	  
	  @Test(enabled = false)
	  public void givenJsonPath_forReadingCreatorName() {
//		  System.out.println(new ReadJsonPath().jsonDataSourceString);
		  String jsonpathCreatorNamePath = "$['tool']['jsonpath']['creator']['name']";
		  String jsonpathCreatorName = jsonContext.read(jsonpathCreatorNamePath);
		  System.out.println("Creator Name:=>"+jsonpathCreatorName);
		  assertEquals("Jayway Inc.", jsonpathCreatorName);
	  }  
	  
	  
	  @Test(enabled = false)
	  public void readJsonArray() {
		  String jsonpathCreatorLocationPath = "$['tool']['jsonpath']['creator']['location'][*]";
		  List<String> jsonpathCreatorLocation = jsonContext.read(jsonpathCreatorLocationPath);
		  
		  for (String string : jsonpathCreatorLocation) {
			System.out.println("Location :=>"+string);
		}
		  System.out.println(jsonpathCreatorLocation.toString());
//		  assertEquals(jsonpathCreatorLocation.toString(), containsString("Malmo"));
		  assertThat(jsonpathCreatorLocation.toString(), containsString("Malmo"));
		  assertThat(jsonpathCreatorLocation.toString(), containsString("San Francisco"));
		  assertThat(jsonpathCreatorLocation.toString(), containsString("Helsingborg"));
	  }
	  
	 @Test(enabled = false)
	 public void findmin_val_inJsonArray() {
		 
		 String jsonpathCreatorLocationPath = "$['book'][*]['price']";
		 String jsonpathforminBookPrice = "$.book..price.min()";
		 
//		  List<String> jsonpathCreatorLocation = jsonContext.read(jsonpathCreatorLocationPath);
		  
		  System.out.println(jsonContext.read(jsonpathforminBookPrice).toString());
	 }


}
