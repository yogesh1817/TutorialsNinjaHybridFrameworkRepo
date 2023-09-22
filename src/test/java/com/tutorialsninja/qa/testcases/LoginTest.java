 package com.tutorialsninja.qa.testcases;

import java.time.Duration;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.tutorialsNinja.qa.pages.AccountPage;
import com.tutorialsNinja.qa.pages.HomePage;
import com.tutorialsNinja.qa.pages.LoginPage;
import com.tutorialsNinja.qa.utils.Utilities;
import com.tutorialsninja.qa.base.Base;

public class LoginTest extends Base {
	
	public WebDriver driver;
	
	LoginPage loginPage;
	
	public LoginTest() {
		super();
	}
	 
	@BeforeMethod
	public void setUp() {
		
		driver=initializeBrowserAndOpenApplication(prop.getProperty("browserName"));
		HomePage homePage=new HomePage(driver);
		loginPage=homePage.navigateToLoginPage();
		}
	
	@AfterMethod
	public void tearDown() {
		driver.quit();
	}
	
	@Test(priority =1,dataProvider = "validCredentialSupplier")
	public void verifyLoginWithValidCredentials(String Email,String Password) {
		
		AccountPage accountPage = loginPage.login(Email, Password);
		Assert.assertTrue(accountPage.getDisplayStatusOfEditYourAccountInfoOption());
	}
	
	
	@DataProvider(name="validCredentialSupplier")
	public Object[][] supplyTestData() {
		Object[][] data=Utilities.getTestDataFromExcel("Login");
       return data;
	}
	
	@Test(priority = 2)
	public void verifyLoginWithInvalidCredentials() {
		
		loginPage.login(Utilities.generateEmailWithTimeStamp(),dataProp.getProperty("invalidPassword"));
		Assert.assertTrue(loginPage.retrieveEmailPasswordNotMatchingWarningMsgText().contains(dataProp.getProperty("emailPasswordNoMatchWarning")));
		}
	
	@Test(priority = 3)
	public void verifyLoginWithInvalidEmailAndValidPassword() {
		
		loginPage.login(Utilities.generateEmailWithTimeStamp(),"validPassword");
		Assert.assertTrue(loginPage.retrieveEmailPasswordNotMatchingWarningMsgText().contains(dataProp.getProperty("emailPasswordNoMatchWarning")));
		}
	
	@Test(priority = 4)
	public void verifyLoginWithValidEmailAndInvalidPassword() {
		
		loginPage.login("validEmail", "invalidPassword");
	    Assert.assertTrue(loginPage.retrieveEmailPasswordNotMatchingWarningMsgText().contains(dataProp.getProperty("emailPasswordNoMatchWarning")));
	}
	@Test(priority = 5)
	public void verifyLoginWithoutProvidingCredentials() {
        loginPage.clickOnLoginButton();
		Assert.assertTrue(loginPage.retrieveEmailPasswordNotMatchingWarningMsgText().contains(dataProp.getProperty("emailPasswordNoMatchWarning")));
		
	}
	

}
