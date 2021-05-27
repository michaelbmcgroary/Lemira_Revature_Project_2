package com.revature;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.revature.pages.GlbHomepage;
import com.revature.pages.GlbLoginPage;
import com.revature.pages.GlbSearchGamePage;
import com.revature.pages.GlbSignupPage;

public class Project2AutomationTest {
	
	
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
	
//	@Test
//	@Order(1)
//	void test_title_10RecentGameReview() {
//		String expected = "10 Newest Game Reviews".trim();
//		String actual = glbHomepage.getTenRecentGameReviews();
//		assertEquals(expected,actual);
//	}
	
	
	@Test
	@Order(2)
	void test_loginButtonClick() {
		
	
		glbHomepage.clickLogin();
		
		String expected = "Username".trim();
		String actual = glbLoginPage.getUsernameText();
		
		assertEquals(expected, actual);
	}
	
	
	
	@Test
	@Order(4)
	void test_signupButtonClick() {
	
		glbHomepage.clickSignup();
		
		String expected = "Username".trim();
		String actual = glbSignupPage.getUsernameText();
		
		assertEquals(expected, actual);
	}
	
	@Test
	@Order(5)
	void test_searchGameButtonClick() {
		glbHomepage.clickSearchGame();
		
		String expected = "Search Game".trim();
		String actual = glbSearchGamePage.getSearchGameText();
		assertEquals(expected, actual);
		
	}
	
	
	
	@AfterAll
	static void tearDownAfterClass() {
		driver.close();
	}

}
