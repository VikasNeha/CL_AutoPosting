package com.PageEvents;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//import javax.swing.JOptionPane;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.Main.SuperInputContent;
import com.Main.Utilities;

public class CLEvents extends WebPageEvents{

	public CLEvents(WebDriver driverToSetup) {
		super(driverToSetup);
	}
	
	public void openURL(String URL) {
		super.openURL(URL);
	}
	
	public void loginAs(String Email, String Password) throws MyException {
		this.enterText("inputEmailHandle", Email);
		this.enterText("inputPassword", Password);
		this.clickButton("Log In");
		try {
			this.waitAndFindElement(By.xpath("//input[@value='I ACCEPT']")).click();
		} catch(Exception e) {}
		assertLoginSuccessful();
	}
	
	void assertLoginSuccessful() throws MyException {
		try {
			assertLinkPresent("log out");
		} catch (MyException e) {
			throw new MyException(MyException.LOGIN_UNSUCCESSFUL + "---" + e.getMessage(), e);
		}
	}
	
	public void logout() throws MyException {
		clickLink("logout");
		assertLogoutSuccessful();
	}
	
	void assertLogoutSuccessful() throws MyException {
		try {
			assertTitleContains("log in");
		} catch(MyException e) {
			throw new MyException("Logout Unsuccessful" + "---" + e.getMessage(), e);
		}
	}

	public void tearDown() {
		super.driverQuit();
	}

	public void clickNewPosting() throws MyException {
		super.clickLink("new posting");
	}
	
	public void selectAdLocation(String locationName) throws MyException {
		super.selectFromDropdown("areaabb", locationName);
	    super.findArbitraryElementAndClick(By.tagName("input"), "value", "go");
		/*
		try{
			super.selectFromDropdown("n", locationName);
			//Click Go
			super.findArbitraryElementAndClick(By.tagName("button"), "value", "Continue");
			
		} catch(Exception e) {
			try {
				WebElement formNote = super.waitAndFindElement(By.className("formnote"));
				if (formNote.getText().contains("what type of posting is this"))
					return;
				else 
					throw new MyException("Problem with choosing city", e);
			}
			catch(MyException e2) {
				throw new MyException(e2.getMessage(), e2);
			}
		}*/
	}

	public void selectTypeOfPosting() throws MyException {
		//Select "For Sale - By Dealer"
		super.findArbitraryElementAndClick(By.tagName("input"), "value", "fsd");
	}
	
	public void selectAdCategory() throws MyException {
		// Select Cars & Trucks by Dealer
		super.findArbitraryElementAndClick(By.tagName("input"), "value", "146");
	}

	public void selectSubLocation() throws MyException {
		WebElement formNote;
		try{
			formNote = super.waitAndFindElement(By.className("formnote"));
		}
		catch (MyException e) {
			return;
		}
		if (formNote.getText().contains("choose the location")) {
			WebElement subLocationBox = super.waitAndFindElement(By.tagName("blockquote"));
			subLocationBox.findElements(By.tagName("input")).get(0).click();
		}
	}
	
	public void enterAdDetails(SuperInputContent currentAd) throws MyException {
		try {
			WebElement titleRow = super.waitAndFindElement(By.xpath("//div[@class='title row fields']"));
			List<WebElement> inputFields = titleRow.findElements(By.tagName("input"));
			// Input Title
			inputFields.get(0).sendKeys(currentAd.Title);
			// Input Price
			inputFields.get(1).sendKeys(currentAd.Price);
			// Input Specific Location
			inputFields.get(2).sendKeys(currentAd.Specific_Location);
			
			WebElement adDescription = super.waitAndFindElement(By.id("PostingBody"));
			adDescription.sendKeys(currentAd.Description);
			
			super.selectFromDropdown("auto_year", currentAd.Year);
			super.enterText("auto_make_model", currentAd.MakeModel);
			super.enterText("auto_miles", currentAd.Odometer);
			
			WebElement continueButton = super.waitAndFindElement(By.className("bigbutton"));
			continueButton.click();
		} catch (IndexOutOfBoundsException e) {
			throw new MyException("Problem in interacting with Ad Posting page ---" + e.getMessage(), e);
		}
	}
	
	public void uploadImages(SuperInputContent currentAd) throws MyException {
		if (currentAd.imagePath.isEmpty()) {
			WebElement continueButton = super.waitAndFindElement(By.xpath("//*[@class='done bigbutton']"));
			continueButton.click();
			return;
		}
		
		int numOfImages = countFilesInDir(currentAd.imagePath);
		if (numOfImages == 0) {
			WebElement continueButton = super.waitAndFindElement(By.xpath("//*[@class='done bigbutton']"));
			continueButton.click();
			return;
		}
		
		ArrayList<String> allImageFiles = getPathsOfAllImageFiles(currentAd.imagePath);
		
		int uploadedFileCount = 0;
		for (String currentImageFile : allImageFiles) {
			WebElement browseButton = super.waitAndFindElement(By.className("file"));
			browseButton.sendKeys(currentImageFile);
			//this.clickButton("add image");
			uploadedFileCount++;
			Utilities.waitForSecs(4);
			WaitTillUploadIsComplete(uploadedFileCount);
			if (uploadedFileCount == 8)
				break;
		}
		/*
		try {
			//Runtime.getRuntime().exec("Resources\\FileUpload.exe " + currentAd.imagePath);
			WebElement browseButton = super.waitAndFindElement(By.className("file"));
			//browseButton.click();
			String path = new File(".").getCanonicalPath();
			browseButton.sendKeys(path + "\\" + currentAd.imagePath + "\\101361.jpg");
			System.out.println("--" + path + "\\" + currentAd.imagePath + "\\101361.jpg");
			System.exit(0);
			
			Utilities.waitForSecs(4);
			WaitTillUploadIsComplete(numOfImages);
		} catch (Exception e) {
			throw new MyException("Problem in uploading images ---" + e.getMessage(), e);
		}
		*/
		WebElement continueButton = super.waitAndFindElement(By.xpath("//*[@class='done bigbutton']"));
		continueButton.click();
	}
	
	public void publishAd() throws MyException {
		WebElement continueButton = super.waitAndFindElement(By.className("bigbutton"));
		continueButton.click();
		//assertPublishAd();
	}

    /*
	public void assertPublishAd() throws MyException {
		try {
			WebElement box = super.waitAndFindElement(By.id("thanks"));
			if (box.getText().contains("FURTHER ACTION IS REQUIRED"))
				throw new MyException("Problem with publishing ad");
		} catch (MyException e) {
			throw new MyException("Problem with publishing ad", e);
		}
	}
	*/
	
	public static int countFilesInDir(String dirPath) {
		int count = 0;
		try {
			File f = new File(dirPath);
            //noinspection ConstantConditions
            for (File file : f.listFiles()) {
	        	if (file.isFile()) {
	        		count++;
	            }
	        }
		} catch(NullPointerException e) {
            e.printStackTrace();
        }
        return count;
	}
	
	public static ArrayList<String> getPathsOfAllImageFiles(String dirPath) {
		ArrayList<String> allImageFiles = new ArrayList<String>();
		try {
			File f = new File(dirPath);
            //noinspection ConstantConditions
            for (File file : f.listFiles()) {
	        	if (file.isFile()) {
	        		allImageFiles.add(file.getAbsolutePath());
	            }
	        }
		} catch(NullPointerException e) {}
        return allImageFiles;
	}
 
	public void WaitTillUploadIsComplete(int numOfImages) {
		int countOfSecs = 0;
		do {
			try {
				WebElement imageBox = super.waitAndFindElement(By.className("images"));
				int numOfImagesUploaded;
				numOfImagesUploaded = imageBox.findElements(By.className("imgbox")).size();
				if ((numOfImagesUploaded == numOfImages) || (countOfSecs > 300))
					break;
				else {
					Utilities.waitForSecs(4);
				}
			}
			catch (Exception e) {
                //noinspection UnnecessaryContinue
                continue;
            }
			finally {
				countOfSecs = countOfSecs + 4;
			}
		}while(true);
	}
	
	public boolean isAdActivated(SuperInputContent currentAd) {
		try {
			WebElement body = super.waitAndFindElement(By.className("body"));
			if (body.getText().contains("Thanks for posting with us, we really appreciate it")) {
				currentAd.resultComment = "Ad Posted - Live";
				return true;
			}
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void captureAdUrl(SuperInputContent currentAd) throws MyException {
		try {
			WebElement body = super.waitAndFindElement(By.className("body"));		
			WebElement adURL = body.findElement(By.tagName("a"));
			currentAd.resultComment = adURL.getAttribute("href");			
		} catch (MyException e) {
			throw new MyException("Problem with capturing Ad URL---" + e.getMessage(), e);
		}
	}
	
	public boolean isEmailActivation(SuperInputContent currentAd) {
		try {
			WebElement section = super.waitAndFindElement(By.id("thanks"));
			if (section.getText().contains("IMPORTANT - FURTHER ACTION IS REQUIRED TO COMPLETE YOUR REQUEST")) {
				currentAd.resultComment = "Ad Posted - Email Activation Required";
				return true;
			}
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isPhoneActivation(SuperInputContent currentAd) {
		try {
			WebElement body = super.waitAndFindElement(By.className("body"));
			if (body.getText().contains("Your craigslist user account requires phone verification")) {
				currentAd.resultComment = "Ad Posted - Phone Activation Required";
				return true;
			}
			else 
				return false;
		} catch (Exception e) {
			return false;
		}
	}

    /*
	public void performPhoneActivation(SuperInputContent currentAd) throws MyException {
		String phone = currentAd.phone.substring(0, 10);
		String[] phones = new String[3];
		phones[0] = phone.substring(0, 3);
		phones[1] = phone.substring(3, 6);
		phones[2] = phone.substring(6, 10);
		
		try {
			WebElement section = super.waitAndFindElement(By.className("body"));
			section = section.findElement(By.tagName("fieldset"));
			List<WebElement> inputs = section.findElements(By.tagName("input"));
			int i=0;
			for (WebElement currentInput : inputs) {
				currentInput.sendKeys(phones[i]);
				i++;
				if (i == 3)
					break;
			}
			WebElement button = super.waitAndFindElement(By.name("go"));
			button.click();
			
			String code = JOptionPane.showInputDialog("Enter the code");
			
			section = super.waitAndFindElement(By.className("body"));
			section = section.findElement(By.tagName("form"));
			inputs = section.findElements(By.tagName("input"));
			
			for (WebElement currentInput : inputs) {
				if (currentInput.getAttribute("type").contains("text")) {
					currentInput.sendKeys(code);
					break;
				}
			}
			
			button = super.waitAndFindElement(By.name("go"));
			button.click();
			
		} catch (MyException e) {
			throw new MyException("Problem with Phone Activation ---" + e.getMessage(), e);
		}		
	}
	*/

	public void performEmailActivation(SuperInputContent currentAd, CLEvents clEvents) throws MyException {
		String AdActivationURL = GmailInteractor.getAdActivationURL(currentAd.Email, currentAd.Gmail_Password);
		clEvents.openURL(AdActivationURL);
        //noinspection EmptyCatchBlock
        try {
			WebElement buttonSection = super.waitAndFindElement(By.id("previewButtons"));
			WebElement button = buttonSection.findElement(By.tagName("button"));
			button.click();
		} catch (Exception e) {			
		}
		
		/*
		boolean isPhoneActication = clEvents.isPhoneActivation(currentAd);
		if (isPhoneActication == true) {
			clEvents.performPhoneActivation(currentAd);
		}
		*/
	}
}
