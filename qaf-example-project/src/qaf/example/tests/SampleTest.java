package qaf.example.tests;

import static com.qmetry.qaf.automation.step.CommonStep.click;
import static com.qmetry.qaf.automation.step.CommonStep.get;
import static com.qmetry.qaf.automation.step.CommonStep.sendKeys;
import static com.qmetry.qaf.automation.step.CommonStep.verifyLinkWithTextPresent;

import java.util.Map;

import org.testng.annotations.Test;

import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider;
import com.qmetry.qaf.automation.ui.WebDriverTestCase;
import com.qmetry.qaf.automation.util.Validator;
import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;

public class SampleTest extends WebDriverTestCase {

//	@Test(enabled=false,description="Sample Test Scenario", groups={"SMOKE"})
//	   public void testGoogleSearch() {
//		System.out.println(props.getString("url")); //Read data from Property file
//	   }
//	@QAFDataProvider(dataFile="./resources/data/testdata.csv")
//	@Test(enabled=false,description="data fetching test for csv file")
//	public void login(Map<String,String> data) {
//		System.out.println(data.get("user_name"));// Read data from csv file
//	}
//	
//	@QAFDataProvider(dataFile="./resources/data/logintestdata.json")
//	@Test(enabled=true,description="data fetching from json File")
//	public void readJson(Map<String,String> data) {
//		System.out.println(data.get("username"));
//	}
	@QAFDataProvider(key ="login.data")
	@Test(enabled=true,description="data Fetching from Xml File")
	public void readXml(Map<String,String> data) {
		System.out.println(data.get("user_name"));
	}
}
