package com.citrus.stepdefinitions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.citrus.base.Services;
import com.citrus.util.Fileutil;
import com.citrus.util.oracledbUtil;
import com.cucumber.listener.Reporter;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class BANK_Cof_test_cases {
	
	List<String>body= new ArrayList<String>();
	oracledbUtil odu = new oracledbUtil();
	HashMap<String,String> getdbValue = new HashMap<String,String>();
	SoftAssert sa = new SoftAssert();
	Fileutil fu = new Fileutil();
	Services s = new Services();
	String outPutData=null;
	
	@Given("^id \"([^\"]*)\" key \"([^\"]*)\" card_number \"([^\"]*)\" card_exp_date \"([^\"]*)\"$")
	public void id_key_card_number_card_exp_date(String id, String key, String card_number,String card_exp_date) throws Throwable {
	    body.add(id);
	    body.add(key);
	    body.add(card_number);
	    body.add(card_exp_date);
	    Assert.assertNotNull(body);
  	}

	@When("^transaction_amount \"([^\"]*)\" transaction_type \"([^\"]*)\"$")
	public void transaction_amount_transaction_type(String transaction_amount,String transaction_type) throws Throwable {
		body.add(transaction_amount);
		body.add(transaction_type);
		outPutData=s.postMethod(fu.prop.getProperty("test_end_point_api"),s.appendString(body));
		Assert.assertNotNull(outPutData);
		s.parseResponse(outPutData);
				
	}

	@Then("^transaction_id should not be null$")
	public void transaction_id_should_not_be_null() throws Throwable {
		Reporter.addStepLog("Transaction_ID from API response "+s.api.get("transaction_id"));
		Assert.assertNotNull(s.api.get("transaction_id"));
	}

	@And("^error_code should be \"([^\"]*)\"$")
	public void error_code_should_be(String arg1) throws Throwable {
		Reporter.addStepLog("Error_Code from API response "+s.api.get("error_code"));
		Assert.assertEquals(s.api.get("error_code"),"000");;
	}
	@And("^auth_response_text should be \"([^\"]*)\"$")
	public void auth_response_text_should_be(String arg1) throws Throwable {
		Reporter.addStepLog("Auth_Response_Text from API response "+s.api.get("auth_response_text"));
	//  Assert.assertEquals(arg0, arg1, arg2);
	}
	@And("^auth_code should be present$")
	public void auth_code_should_be_present() throws Throwable {
		Reporter.addStepLog("Auth_Code from API response "+s.api.get("auth_code"));
		Assert.assertNotNull(s.api.get("auth_code"));
	}
	@And("^card_id should be alphanumeric$")
	public void card_id_should_be_alphanumeric() throws Throwable {
		Reporter.addStepLog("Card_ID from API response "+s.api.get("card_id"));
		Assert.assertNotNull(s.api.get("card_id"));
	    
	}
	@And("^database values should be matched with api response$")
	public void database_values_should_be_matched_with_api_response() throws Throwable {
		
		getdbValue=odu.getdatafromDB(s.api.get("transaction_id"));
		sa.assertNotNull(getdbValue.size());
		Reporter.addStepLog("Hash Map size "+getdbValue.size());
		sa.assertEquals(getdbValue.get("AUTHORIZATION_CODE"),s.api.get("auth_code"));
		sa.assertEquals(getdbValue.get("AUTH_RESPONSE_TEXT"),s.api.get("auth_response_text"));
		sa.assertAll();
		
	    
	}
}
