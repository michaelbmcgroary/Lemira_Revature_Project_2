package com.revature.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.revature.dao.UserRepository;
import com.revature.dto.LoginDTO;
import com.revature.dto.MessageDTO;
import com.revature.dto.PostUserDTO;
import com.revature.exception.BadParameterException;
import com.revature.exception.DatabaseException;
import com.revature.exception.LoginException;
import com.revature.exception.PasswordHashException;
import com.revature.exception.UserAddException;
import com.revature.model.User;
import com.revature.service.LoginService;

//@CrossOrigin( allowCredentials = "true" ,origins = "http://localhost:8080")
//@CrossOrigin(allowCredentials = "true", origins = "*")
//@CrossOrigin(origins = "*", allowCredentials = "true")
@CrossOrigin( allowCredentials = "true" ,origins = {"http://localhost:8080", "http://ec2-52-14-217-72.us-east-2.compute.amazonaws.com:8080"})
@Controller
public class LoginController {

	private Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpServletResponse response;
	
	public LoginController() throws DatabaseException {
		this.loginService = new LoginService();
	}
	
	//This one is for Mockito testing
	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}
	
	@PostMapping(path = "login")
	public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO){
		try {
			User user = null;
			user =loginService.login(loginDTO);
			if(user == null) {
				MessageDTO messageDTO = new MessageDTO();
				messageDTO.setMessage("User could not be logged in");
				return ResponseEntity.status(400).body(messageDTO);
			}
			HttpSession session = request.getSession(true);
			session.setAttribute("currentlyLoggedInUser", user);
			return ResponseEntity.status(200).body(user);
		} catch(LoginException e) {
			logger.error("The user could not be found and therefore could not be logged in");
			return ResponseEntity.status(400).body(e.getMessage());
		} catch (BadParameterException e) {
			logger.warn("The user gave a bad parameter and therefore could not be logged in");
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}
	

	@GetMapping(path="user/currentUser")
	public @ResponseBody ResponseEntity<Object> getCurrentUser(){
		User user = null;
		HttpSession session = request.getSession(true);
		user = (User) session.getAttribute("currentlyLoggedInUser");
		if(user == null) {
			MessageDTO messageDTO = new MessageDTO();
			messageDTO.setMessage("User is not currently logged in");
			return ResponseEntity.status(400).body(messageDTO);
		} else {
			return ResponseEntity.status(200).body(user);
		}
	}
	
	@PostMapping(path="logout")
	public @ResponseBody ResponseEntity<Object> logout() {
		HttpSession session = request.getSession(false);
		if(session == null) {
			return ResponseEntity.status(400).body(new MessageDTO("User is not logged in"));
		}
		logger.info("User, " + ((User) session.getAttribute("currentlyLoggedInUser")).getUsername()+ " has logged out");
		session.invalidate();
		return ResponseEntity.status(200).build();
	};
	
	@PostMapping(path="newUser")
	public @ResponseBody ResponseEntity<Object> newUser(@RequestBody PostUserDTO userDTO) {
		//may update the e.getMessage to a full message
		User user = null;
		try {
			user = loginService.createNewUser(userDTO);
		} catch (PasswordHashException e) {
			return ResponseEntity.status(400).body(e.getMessage());
		} catch (UserAddException e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
		if(user == null) {
			MessageDTO messageDTO = new MessageDTO();
			messageDTO.setMessage("User could not be logged in");
			return ResponseEntity.status(400).body(messageDTO);
		}
		return ResponseEntity.status(200).body(user);
	};
	
	
}
