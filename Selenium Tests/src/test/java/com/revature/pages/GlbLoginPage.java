package com.revature.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GlbLoginPage {

	private WebDriver driver;
	
	private By usernameTextLocator = By.xpath("//*[text()='Username']");
	private By usernameInput = By.cssSelector("[type ='text']");
	private By passwordInput = By.cssSelector("[type ='password']");
	
	
	
	
	public GlbLoginPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public String getUsernameText() {
		return this.driver.findElement(usernameTextLocator).getText();
	}
	
	
	
}
