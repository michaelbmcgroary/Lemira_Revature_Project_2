package com.revature.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dto.LoginDTO;
import com.revature.exception.BadParameterException;
import com.revature.exception.DatabaseException;
import com.revature.exception.LoginException;
import com.revature.exception.PasswordHashException;
import com.revature.model.User;
import com.revature.model.UserStatus;
import com.revature.model.UserType;
import com.revature.service.LoginService;
import com.revature.service.ReviewService;


@ExtendWith(SpringExtension.class)
@ContextHierarchy({
	@ContextConfiguration("classpath:applicationContext.xml"),
	@ContextConfiguration("classpath:dispatcherContext.xml")
})
@WebAppConfiguration
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

	private MockMvc mockMvc;
	private ObjectMapper om;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Mock
	LoginService loginService;
	
	@Mock
	private HttpServletRequest request;
	
	@Mock
	private HttpSession httpSession;
	
	@InjectMocks
	LoginController loginController;
	
	@BeforeEach
	void setup() throws BadParameterException, LoginException, PasswordHashException {
		
		LoginDTO login = new LoginDTO("Username", "Password");
		User user = new User(1, "Username", "Password", "George", "Lucas", "GLucas@gmail.com", new UserType(1), new UserStatus(1));
		
		when(request.getSession(true)).thenReturn(httpSession);
		
		when(loginService.login(eq(new LoginDTO("Username", "Password")))).thenReturn(user);
		
		this.mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
		this.om = new ObjectMapper();
	}
	
	@Test
	void test_login_NoIssue() throws Exception {
		
		MockHttpSession session = new MockHttpSession();
		
		String body = "{\"username\":\"Username\",\"password\":\"Password\"}";
		User expected = new User(1, "Username", "Password", "George", "Lucas", "GLucas@gmail.com", new UserType(1), new UserStatus(1));
		
		
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
				.post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body)
				.session(session);
		

		String expectedUserJson = om.writeValueAsString(expected);
		
		System.out.println("ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
		System.out.println(expectedUserJson);
		System.out.println(body);
		
		MvcResult result = this.mockMvc
				.perform(builder)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json(expectedUserJson)).andReturn();
		
		System.out.println("100% of the time, this should display");
		System.out.println(result.getResponse().getContentAsString());
		
	}
	
	/*
	@Test
	void test_login_blankLogin() throws Exception {
		User expected = new User(1, "Username", "Password", "George", "Lucas", "GLucas@gmail.com", new UserType(1), new UserStatus(1));
		ObjectMapper om = new ObjectMapper();
		LoginDTO login = new LoginDTO("Username", "Password");
		String expectedUserJson = om.writeValueAsString(expected);
		String body = om.writeValueAsString(login);
		
		this.mockMvc
				.perform(post("/login"))
				.andExpect(MockMvcResultMatchers.status().is(400))
				.andExpect(MockMvcResultMatchers.content().json(expectedUserJson));
	}
	*/

}
