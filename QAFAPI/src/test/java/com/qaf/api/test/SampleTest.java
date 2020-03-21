package com.qaf.api.test;

import static com.qaf.api.steps.StepsLibrary.searchFor;
import static com.qmetry.qaf.automation.step.CommonStep.get;
import static com.qmetry.qaf.automation.step.CommonStep.verifyLinkWithPartialTextPresent;

import org.testng.annotations.Test;

import com.qmetry.qaf.automation.ui.WebDriverTestCase;

public class SampleTest extends WebDriverTestCase {

	@Test
	public void testGoogleSearch() {
		get("/");
		searchFor("qaf github infostretch");
		verifyLinkWithPartialTextPresent("qaf");
	}
}
