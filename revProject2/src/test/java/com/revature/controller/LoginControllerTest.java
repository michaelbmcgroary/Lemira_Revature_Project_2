package com.revature.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import com.revature.exception.DatabaseException;
import com.revature.service.LoginService;
import com.revature.service.ReviewService;

class LoginControllerTest {

	@InjectMocks
	
	@Autowired
	private LoginController loginController;
	
	private static LoginService mockLoginService;
	
	@BeforeAll
	public static void setUp() {
		mockLoginService = mock(LoginService.class);
	}
	
	@BeforeEach
	public void beforeTest() throws DatabaseException {
		loginController = new LoginController();
	}
	
	@Test
	void test_login_NoIssue() {
		
	}
	
	//bad username/password
	//bad parameter
	
	@Test
	void test_getCurrentUser_NoIssue() {
		
	}
	
	

}
