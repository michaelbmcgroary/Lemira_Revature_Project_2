package com.revature.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GlbSearchGamePage {
	private WebDriver driver;
	
	
	
	private By searchGameTextLocator = By.xpath("//*[text()='Search Game']");
	
	public GlbSearchGamePage(WebDriver driver) {
		this.driver = driver;
	}
	
	public String getSearchGameText() {
		return this.driver.findElement(searchGameTextLocator).getText();
	}
}
