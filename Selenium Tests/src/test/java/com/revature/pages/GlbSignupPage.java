package com.revature.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GlbSignupPage {

	
	private WebDriver driver;
	
	private By usernameTextLocator = By.xpath("//*[text()='Username']");
	private By usernameInput = By.cssSelector("[placeholder='Username']");
	private By firstNameInput = By.cssSelector("[placeholder='First Name']");
	private By lastNameInput = By.cssSelector("[placeholder='Last Name']");
	private By emailInput = By.cssSelector("[placeholder='Email']");
	private By passwordInput = By.cssSelector("[placeholder='Password']");
	private By confirmPasswordInput = By.cssSelector("[placeholder='Confirm Password']");
	private By submitButton = By.xpath("//*[text()='Submit']");
	
	
	
	
	public GlbSignupPage(WebDriver driver) {
		this.driver = driver;
	}
	
	
	public String getUsernameText() {
		return this.driver.findElement(usernameTextLocator).getText();
	}
	
	public void typeUsername(String username) {
		this.driver.findElement(usernameInput).sendKeys(username);
	}
	
	public void typeFirstName(String firstName) {
		this.driver.findElement(firstNameInput).sendKeys(firstName);
	}
	
	public void typeLastName(String lastName) {
		this.driver.findElement(lastNameInput).sendKeys(lastName);
	}
	
	
	public void typeEmail(String email) {
		this.driver.findElement(emailInput).sendKeys(email);
	}
	
	public void typePassword(String password) {
		this.driver.findElement(passwordInput).sendKeys(password);
	}
	
	public void typeConfirm(String confirm) {
		this.driver.findElement(confirmPasswordInput).sendKeys(confirm);
	}
	
	public void submitClick() {
		this.driver.findElement(submitButton).click();
	}

}
