package com.Main;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.PageEvents.CLEvents;
import com.PageEvents.MyException;

public class PostAds {
	
	static void PostAllAds() {
		Logger LOGGER = Logger.getLogger(CL_AutoPosting.class.getName());
		//Loop through all ads		
		for (SuperInputContent currentAd : Utilities.allAds) {
			//Ignore if current ad has already failed
			if ((currentAd.result == false) && (currentAd.resultComment != "Not Processed"))
				return;
			
			//------------------------------------------------------------------- Get Proxy and Launch Firefox Driver 
			String PROXY = currentAd.Proxy.trim() + ":" + currentAd.Proxy_Port;
			WebDriver driver = setupDriverWithProxyForCurrentAd(PROXY);			//Setup webdriver
			CLEvents clEvents = new CLEvents(driver);
			try {
				postOneAd(currentAd, clEvents);
				currentAd.result = true;
				boolean isAdActivated = clEvents.isAdActivated(currentAd);
				if (isAdActivated == true)
					clEvents.captureAdUrl(currentAd);
				else if (isAdActivated == false) {
					boolean isEmailActivation = clEvents.isEmailActivation(currentAd);
					if (isEmailActivation == true) {
						Utilities.waitForSecs(5);
						clEvents.performEmailActivation(currentAd, clEvents);
						isAdActivated = clEvents.isAdActivated(currentAd);
						if (isAdActivated == true)
							clEvents.captureAdUrl(currentAd);
					}
					else if (isEmailActivation == false) {
						boolean isPhoneActication = clEvents.isPhoneActivation(currentAd);
						if (isPhoneActication == true) {
							/*
							clEvents.performPhoneActivation(currentAd);
							isAdActivated = clEvents.isAdActivated(currentAd);
							if (isAdActivated == true)
								clEvents.captureAdUrl(currentAd);
								*/
						}
						else if (isPhoneActication == false)
							currentAd.resultComment = "Posted";
					}
				}
				takeScreenshot(currentAd, clEvents);
				clEvents.logout();
			} catch (MyException e) {
				currentAd.result = false;
				currentAd.resultComment = e.getMessage();
				LOGGER.severe(e.getMessage() + "---" + org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
				takeScreenshot(currentAd, clEvents);
			} catch (Exception e) {
				currentAd.result = false;
				currentAd.resultComment = "UNEXPECTED ERROR OCCURRED ---" + e.getMessage();
				LOGGER.severe(e.getMessage() + "---" + org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
				takeScreenshot(currentAd, clEvents);
				return;
			}
			finally {				
				clEvents.tearDown();
			}
		}
	}
	
	private static void loginAccountToCL(CLEvents clEvents, SuperInputContent currentAd) throws MyException {
		try {
			clEvents.loginAs(currentAd.Email, currentAd.CL_Password);
		} catch (MyException e) {
			currentAd.result = false;
			currentAd.resultComment = e.getMessage();
			throw new MyException(e.getMessage(), e);
		}
	}
	
	private static WebDriver setupDriverWithProxyForCurrentAd(String PROXY) {
		if (PROXY.trim().equals(":"))
			return new FirefoxDriver();
		else {
			Proxy proxy = new Proxy();
			proxy.setHttpProxy(PROXY);
			proxy.setHttpsProxy(PROXY);
			proxy.setFtpProxy(PROXY);
			proxy.setSslProxy(PROXY);
			DesiredCapabilities cap = new DesiredCapabilities();
			cap.setCapability(CapabilityType.PROXY, proxy);
			return new FirefoxDriver(cap);
		}
	}

	private static void postOneAd(SuperInputContent currentAd, CLEvents clEvents) throws MyException {
				
		//------------------------------------------------------------------- Open Craigslist Login Page
		try {
			Runtime.getRuntime().exec("Resources\\Proxy_Auth.exe " + currentAd.Proxy_Username + " " + currentAd.Proxy_Password);
		} catch (IOException e) {
			throw new MyException("Problem in entering proxy details ---" + e.getMessage(), e);
		}
		clEvents.openURL(Utilities.props.getProperty("baseURL")); 
		
		//------------------------------------------------------------------- Successfully Login with an account
		loginAccountToCL(clEvents, currentAd);
		
		//------------------------------------------------------------------- Click 'New Posting'
		//clEvents.clickNewPosting();
		
		//------------------------------------------------------------------- Select Location
		clEvents.selectAdLocation(currentAd.Location);
		
		//------------------------------------------------------------------- Select Type of Posting
		clEvents.selectTypeOfPosting();
		
		//------------------------------------------------------------------- Select Ad Category
		clEvents.selectAdCategory();
		
		//------------------------------------------------------------------- Select SubLocation (if available)
		clEvents.selectSubLocation();
		
		//------------------------------------------------------------------- Enter Ad details
		clEvents.enterAdDetails(currentAd);
		clEvents.uploadImages(currentAd);
		clEvents.publishAd();
		
		//------------------------------------------------------------------- Logout from CL
		//clEvents.driver.quit();
		
	}

	private static void takeScreenshot(SuperInputContent currentAd, CLEvents clEvents) {
		try {
			File scrFile = ((TakesScreenshot)clEvents.driver).getScreenshotAs(OutputType.FILE);
			String workingDir = Utilities.props.getProperty("OutputImagePath");
			String dateStr = new SimpleDateFormat("yyyy-MM-dd").format( Calendar.getInstance().getTime());
			currentAd.outputImagePath = workingDir + "\\" + currentAd.imageFolder + "\\" + currentAd.imageFolder + dateStr + ".png";
			FileUtils.copyFile(scrFile, new File(currentAd.outputImagePath));
		} catch(IOException e) {
			Logger LOGGER = Logger.getLogger(CL_AutoPosting.class.getName());
			LOGGER.severe("Problem in taking output image");
		}
	}

}
