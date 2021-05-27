package com.revature.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumPracticeNothingToDoWithProject2 {

		public static void main(String[] args) {
			
			System.setProperty("webdriver.chrome.driver","C:\\Users\\Ray\\Desktop\\Week-6 demos\\Project2_SeleniumTesting\\src\\main\\resources\\chromedriver.exe" );
			
			WebDriver driver = new ChromeDriver();
			
			driver.get("http://google.com");
			
			// WebElement googleSearchInput = driver.findElement(By.name("q")); // method 1
			//WebElement googleSearchInput = driver.findElement(By.cssSelector("[name='q']")); //method 2
			
		
			WebElement googleSearchInput = driver.findElement(By.xpath("//input[@name='q']")); // Using xpath
			//driver.close(); //close the driver therefore close the browser 
			
			googleSearchInput.sendKeys("selenium");
			WebDriverWait wdw = new WebDriverWait(driver, 5);
			
			// Wait for an element to appear before trying to find the element, so we don't get some sort of exception
			// like ElementNotVisible
			wdw.until(ExpectedConditions.visibilityOfElementLocated(By.name("btnK")));
			WebElement googleSearchButton = driver.findElement(By.name("btnK"));
			
			// Waiting for an element to be clickable, if it's currently not clickable
			wdw.until(ExpectedConditions.elementToBeClickable(googleSearchButton));
			googleSearchButton.click();
			
			WebElement seleniumTextElement = driver.findElement(By.xpath("//div[@id='wp-tabs-container']/descendant::span[text()='Selenium']"));
			// Extracting text from an element
			System.out.println(seleniumTextElement.getText());
		}
}
