package com.tutorialsNinja.qa.listeners;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.tutorialsNinja.qa.utils.MyExtentReporter;
import com.tutorialsNinja.qa.utils.Utilities;

public class MyListners implements ITestListener {
	 ExtentReports extentReport;
	 ExtentTest extentTest;


	@Override
	public void onStart(ITestContext context) {
	   extentReport = MyExtentReporter.generateExtentReport();
	   }


	@Override
	public void onTestStart(ITestResult result) {
		 
		 extentTest = extentReport.createTest(result.getName());
		extentTest.log(Status.INFO, result.getName()+"started executing");
		
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		extentTest.log(Status.PASS, result.getName()+"got successfully executed");
     }

	@Override
	public void onTestFailure(ITestResult result) {
	
		WebDriver driver=null;
		
		try {
			 driver=(WebDriver)result.getTestClass().getRealClass().getDeclaredField("driver").get(result.getInstance());
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		
		String destinationScreenShotPath=Utilities.captureScreenShot(driver, result.getName());
		
		extentTest.addScreenCaptureFromPath(destinationScreenShotPath);
		extentTest.log(Status.INFO,result.getThrowable());
		extentTest.log(Status.FAIL, result.getName()+"got failed");
		
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		
		extentTest.log(Status.INFO, result.getThrowable());
		extentTest.log(Status.SKIP, result.getName()+"got skipped");
		}

	@Override
	public void onFinish(ITestContext context) {
		extentReport.flush();
		
		String pathOfExtentReport=System.getProperty("user.dir")+"\\test-output\\ExtentReports\\extentReport.html";
		File extentReport=new File(pathOfExtentReport);
		try {
			Desktop.getDesktop().browse(extentReport.toURI());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	

}
