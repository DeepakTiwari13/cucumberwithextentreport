package com.citrus.test.runner;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.*;
import cucumber.api.testng.CucumberFeatureWrapper;
import java.io.File;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.citrus.util.oracledbUtil;
import com.cucumber.listener.ExtentProperties;
import com.cucumber.listener.Reporter;

 
@CucumberOptions(
        features = "resources/features/zone_one.feature",
        glue = {"com.citrus.stepdefinitions"},
        tags = {"~@Ignore"},
        plugin = {"pretty", "html:target/html-reports/",
        		"com.cucumber.listener.ExtentCucumberFormatter:target/cucumber-reports/report.html"})
		      
       
public class BANK_Cof_Runner {
	oracledbUtil odu = new oracledbUtil();
    private TestNGCucumberRunner testNGCucumberRunner;
 
    @BeforeClass(alwaysRun = true)
    public void setUpClass() throws Exception {
    	ExtentProperties extentProperties = ExtentProperties.INSTANCE;
    	extentProperties.setReportPath("output/report.html");
    	testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
    	odu.makeconnectionwithDB();
    }
 
    @Test(groups = "cucumber", description = "Runs Cucumber Feature", dataProvider = "features")
    public void feature(CucumberFeatureWrapper cucumberFeature) {
    	testNGCucumberRunner.runCucumber(cucumberFeature.getCucumberFeature());
    }
 
    @DataProvider
    public Object[][] features() {
    	return testNGCucumberRunner.provideFeatures();
    }
 
    @AfterClass(alwaysRun = true)
    public void tearDownClass() throws Exception {
    	Reporter.loadXMLConfig(new File("src/test/resources/extent-config.xml"));
    	Reporter.setSystemInfo("user", System.getProperty("user.name"));
        Reporter.setSystemInfo("os", "Windows 10");
    //  Reporter.setTestRunnerOutput("Sample test runner output message");
        odu.closeconnectionwithDB();
        testNGCucumberRunner.finish();
    }
}