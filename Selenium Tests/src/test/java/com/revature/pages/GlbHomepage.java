package com.revature.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GlbHomepage {
	
	private WebDriver driver;
	
	//Define element locators here
	private By loginLocator = By.id("login");
	private By signupLocator = By.id("signup");
	private By tenRecentGameReviews = By.xpath("//*[text()='10 Newest Game Reviews']");
	private By searchGameLocator = By.cssSelector("[routerlink ='searchGame']");
	
	
	public GlbHomepage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void clickLogin() {
		this.driver.findElement(loginLocator).click();
	}
	
	public void clickSignup() {
		this.driver.findElement(signupLocator).click();
	}
	
	public String getTenRecentGameReviews() {
		return this.driver.findElement(tenRecentGameReviews).getText();
	}
	
	public String getTitle() {
		return this.driver.getTitle();
	}
	
	public void clickSearchGame() {
		this.driver.findElement(searchGameLocator).click();
	}
}
