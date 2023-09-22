package com.tutorialsninja.qa.testcases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tutorialsNinja.qa.pages.HomePage;
import com.tutorialsNinja.qa.pages.SearchPage;
import com.tutorialsninja.qa.base.Base;

public class SearchTest extends Base {
	
public WebDriver driver;
HomePage homePage;

SearchPage searchPage;

    public SearchTest() {
	 super();
    }
	
	@BeforeMethod
	public void setUp() {
		
		driver=initializeBrowserAndOpenApplication(prop.getProperty("browserName"));
		 homePage=new HomePage(driver);
	}
	
	@AfterMethod
	public void tearDown() {
		driver.quit();
	}
	
	
	@Test(priority = 1)
	public void verifySearchWithValidProduct() {
		
		searchPage=homePage.searchForAProduct(dataProp.getProperty("validProduct"));
		Assert.assertTrue(searchPage.displayStatusOfHpProduct());
		
	}
	
	@Test(priority = 2)
	public void verifySearchWithInvalidProduct() {
		searchPage=homePage.searchForAProduct(dataProp.getProperty("invalidProduct"));
		Assert.assertEquals(searchPage.retrieveNoProductMessageText(),"abcd", dataProp.getProperty("noProductTextInSearchResults"));
	}

	@Test(priority = 3,dependsOnMethods = {"verifySearchWithInvalidProduct","verifySearchWithValidProduct"})
	public void verifySearchWithoutAnyProduct() {
		
		searchPage=homePage.clickOnSearchButton();
		Assert.assertEquals(searchPage.retrieveNoProductMessageText(), dataProp.getProperty("noProductTextInSearchResults"));

		
	}
}
