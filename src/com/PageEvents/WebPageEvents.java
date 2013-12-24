package com.PageEvents;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.Main.Utilities;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;

public class WebPageEvents {
	public WebDriver driver;
	
	public WebPageEvents(WebDriver driverToSetup) {
		driver = driverToSetup;
	}
	
	void openURL(String URL) {
		driver.get(URL);
	}
	
	void enterText(String elementID, String text) throws MyException {
		try {
			WebElement element = waitAndFindElement(By.id(elementID));
			element.clear();
			element.sendKeys(text);
		} catch (MyException e) {
			throw new MyException(e.getMessage() + " : " + elementID, e);
		}
	}
	
	WebElement waitAndFindElement(By by) throws MyException {
		try {
			WebElement element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(by));
			return element;
		} catch (ElementNotFoundException | ElementNotVisibleException | NoSuchElementException | TimeoutException e) {
			throw new MyException("Problem with WebElement", e);
		}
	}

	void clickButton(String text) throws MyException {
		List<WebElement> allButtons = driver.findElements(By.tagName("button"));
		boolean buttonFound = false;
		for (WebElement currentButton : allButtons) {
			if (currentButton.getText().equalsIgnoreCase(text)) {
				currentButton.click();
				buttonFound = true;
				break;
			}
		}
		if (buttonFound == false) {
			throw new MyException("Problem in finding and clicking button: " + text);
		}
	}

	void assertLinkPresent(String linkText) throws MyException {
		try {
			waitAndFindElement(By.linkText(linkText));
		} catch (MyException e) {
			throw new MyException(e.getMessage() + " : " + linkText, e);
		}
	}
	
	void clickLink(String linkText) throws MyException {
		try {
			WebElement element = waitAndFindElement(By.partialLinkText(linkText));
			element.click();
		} catch (MyException e) {
			throw new MyException(e.getMessage() + " : " + linkText, e);
		}
	}

	void selectFromDropdown(String elementID, String optionValue) throws MyException {
		boolean optionFoundFlag = false;
		try {
			WebElement element = waitAndFindElement(By.name(elementID));
			List<WebElement> options = element.findElements(By.tagName("option"));
			for (WebElement currentOption : options) {
				if (currentOption.getText().trim().toLowerCase().contains(optionValue.trim().toLowerCase())) {
					currentOption.click();
					optionFoundFlag = true;
					return;
				}
			}
			if (optionFoundFlag == false)
				throw new MyException("Option : " + optionValue + " not found under dropdown");
		} catch (MyException e) {
			throw new MyException(e.getMessage() + " : " + elementID, e);
		}
	}
	
	void assertTitleContains(String text) throws MyException {
		boolean titleFound = false;
		for (int i=0; i<5; i++) {
			Utilities.waitForSecs(1);
			if (driver.getTitle().contains(text)) {
				titleFound = true;
				break;
			}			
		}
		if (titleFound == false) {
			throw new MyException("Title Assertion Failed. Title does not contain text: " + text);
		}
	}
	
	public void findArbitraryElementAndClick(By by, String attributeName, String attributeValue) throws MyException {
		boolean elemFound = false;
		List<WebElement> allItems = this.driver.findElements(by);
		for (WebElement currentItem : allItems) {
			if (currentItem.getAttribute(attributeName).equalsIgnoreCase(attributeValue)) {
				currentItem.click();
				elemFound = true;
				return;
			}
		}
		if (elemFound == false) {
			throw new MyException("Element not found: " + by.toString() + "-" + attributeName + "-" + attributeValue);
		}
	}

	void driverQuit() {
		driver.quit();
	}
}