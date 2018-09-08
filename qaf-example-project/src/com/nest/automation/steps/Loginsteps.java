package com.nest.automation.steps;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.step.CommonStep;
import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.ui.WebDriverTestBase;
import com.qmetry.qaf.automation.ui.webdriver.QAFExtendedWebElement;
import com.qmetry.qaf.automation.ui.webdriver.QAFWebElement;
import com.qmetry.qaf.automation.util.StringMatcher;

public class Loginsteps {

	@QAFTestStep(description="I open the nest application")
	public void launchApp() {
		new WebDriverTestBase().getDriver().get("/");
		
//		CommonStep.get("/");
	}
	@QAFTestStep(description="I login with {username} and password {password}")
	public void login(String username, String password) {
		CommonStep.sendKeys(username, "login.username.txtBx");
		CommonStep.sendKeys(password, "login.password.txtBx");
		CommonStep.click("login.submit.Btn");
	}
	
	@QAFTestStep(description = "I should be on homepage {username}")
	public void homePageTest(String username) {
		QAFWebElement element = new QAFExtendedWebElement(String.format(ConfigurationManager.getBundle().getString("home.user.label"), username));
		element.verifyText(StringMatcher.contains(username));
//		CommonStep.verifyText("homepage.profile.name", "abc pqr");
	
//	@QAFTestStep(description="It should be on homepage")
//	public void verifyLogin() {
//		
//		CommonStep.assertPresent("home.logo.img");
//	}
	
}
	@QAFTestStep(description="I Logout from APP")
	public void logoutFromApp() {
		QAFWebElement logoutWbElement=new QAFExtendedWebElement(ConfigurationManager.getBundle().getString("home.logout.btn"));
		logoutWbElement.waitForVisible(200);
		logoutWbElement.waitForPresent();
		logoutWbElement.click();
	}
	
}
