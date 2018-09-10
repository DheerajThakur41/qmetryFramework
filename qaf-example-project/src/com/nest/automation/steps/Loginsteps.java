package com.nest.automation.steps;

import org.testng.Assert;


import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.step.CommonStep;
import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.WebDriverTestCase;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;
import com.qmetry.qaf.automation.util.Reporter;
import com.qmetry.qaf.automation.util.StringMatcher;

public class Loginsteps  {
	
	
	@QAFTestStep(description="I open the nest application")
	public void iOpenNestApplication() {
		new WebDriverTestBase().getDriver().get("/");
//		CommonStep.get("/");
	}
	@QAFTestStep(description="I login with {username} and password {password}")
	public void login(String username, String password) {
		CommonStep.sendKeys(username, "login.username.txtBx");
		CommonStep.sendKeys(password, "login.password.txtBx");
		CommonStep.click("login.submit.Btn");
//		ConfigurationManager.getBundle().setProperty("loggedin.user",username);
	}
	
	@QAFTestStep(description = "I should be on {name} homepage")
	public void homePageTest(String name) {
		ConfigurationManager.getBundle().setProperty("loggedin.user",name);
		QAFWebElement element = new QAFExtendedWebElement(String.format(ConfigurationManager.getBundle().getString("home.user.label"), name));
		element.verifyText(StringMatcher.contains(name));//Verify Username after Login
//	CommonStep.verifyPresent("home.logo.img");
}
	@QAFTestStep(description="I Logout from APP")
	public void logoutFromApp() {
//		QAFWebElement loadIconElement=new QAFExtendedWebElement(ConfigurationManager.getBundle().getString("home.load.icon"));
//		loadIconElement.waitForNotVisible();
		Reporter.log(ConfigurationManager.getBundle().getString("loggedin.user")+" Has sucessufully Logged in");
		CommonStep.waitForNotVisible("home.load.icon");
//		logoutWbElement.waitForPresent();
		CommonStep.click("home.logout.btn");
		CommonStep.verifyPresent("login.form.Box");//Verify for Login Page 
	}
	@QAFTestStep(description="It should show ERROR {error_msg}")
	public void verifyInvalidLogin(String expMsg)
	{
		String actMsg = CommonStep.getText("login.errorMsg.label");
		Assert.assertEquals(actMsg, expMsg);
		Reporter.logWithScreenShot("Error msg Displayed");
//		import com.qmetry.qaf.automation.util.Reporter;
	}
	
}
