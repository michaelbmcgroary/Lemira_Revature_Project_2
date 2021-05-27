package com.revature;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.revature.pages.GlbHomepage;
import com.revature.pages.GlbLoginPage;
import com.revature.pages.GlbSearchGamePage;
import com.revature.pages.GlbSignupPage;

public class SignUpTest {
	
	private static WebDriver driver;
	private static GlbLoginPage glbLoginPage;
	private static GlbHomepage glbHomepage;
	private static GlbSignupPage glbSignupPage;
	private static GlbSearchGamePage glbSearchGamePage;
	private static WebDriverWait wdw;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		System.setProperty("webdriver.chrome.driver","C:\\Users\\Ray\\Desktop\\Week-6 demos\\Project2_SeleniumTesting\\src\\main\\resources\\chromedriver.exe" );

		driver = new ChromeDriver();
		driver.get("http://ec2-52-14-217-72.us-east-2.compute.amazonaws.com:8080/project2/");
		  driver.manage().window().maximize();
		glbHomepage = new GlbHomepage(driver);
		glbLoginPage = new GlbLoginPage(driver);
		glbSignupPage = new GlbSignupPage(driver);
		glbSearchGamePage = new GlbSearchGamePage(driver);
		 wdw = new WebDriverWait(driver, 5);
		
		
	}

	
	@Test
	@Order(1)
	void test_title_isCorrect() {
		
		String expected = "Project2".trim();
		
		assertEquals(expected, glbHomepage.getTitle());
		
	}
	
	@Test
	@Order(2)
	void test_valid_signup() {
		glbHomepage.clickSignup();
		glbSignupPage.typeUsername("wertwert15001");
		glbSignupPage.typeFirstName("phil");
		glbSignupPage.typeLastName("jenkins");
		glbSignupPage.typeEmail("jenkins111323@gmail.com");
		glbSignupPage.typePassword("jenkins");
		glbSignupPage.typeConfirm("jenkins");
		glbSignupPage.submitClick();
		
		String expected = "Username";
		String actual = glbLoginPage.getUsernameText();
		assertEquals(expected, actual);
		
	}
	
	
	
	@AfterAll
	static void tearDownAfterClass() {
		driver.close();
	}

}
