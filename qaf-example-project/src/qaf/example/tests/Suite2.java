package qaf.example.tests;

import org.testng.annotations.Test;

import com.qmetry.qaf.automation.ui.WebDriverTestCase;
import static com.qmetry.qaf.automation.step.CommonStep.*;
public class Suite2 extends WebDriverTestCase{

	@Test(description="test Google Search",groups= {"SMOKE"})
	public void testGoogleSearch() {
		get("/");
		sendKeys("Git Repository QAF", "txt.searchbox");
		System.out.println("txt.searchbox");
		click("btn.search");
		verifyLinkWithPartialTextPresent("qaf");
	}
}
