package qaf.example.tests;

import static com.qmetry.qaf.automation.step.CommonStep.*;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.ui.WebDriverTestCase;

public class Suite1 extends WebDriverTestCase{
	@Test(description="Sample Test Scenario", groups={"SMOKE"})
	   public void testGoogleSearch() {
	   get("/");
	   sendKeys("Git repository QAF", "txt.searchbox");
	   click("btn.search");
	   verifyLinkWithPartialTextPresent("qmetry/qaf");
	   }
//	@Test(description="Flipkart Test ", groups= {"DRY RUN"})
//	public void testFlipkart() {
//		get("/");
//		sendKeys("FlipKart", "");
//	}
}
