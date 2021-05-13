package com.revature.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revature.dao.UserRepository;
import com.revature.dto.LoginDTO;
import com.revature.dto.PostUserDTO;
import com.revature.exception.BadParameterException;
import com.revature.exception.BadPasswordException;
import com.revature.exception.DatabaseException;
import com.revature.exception.LoginException;
import com.revature.exception.PasswordHashException;
import com.revature.exception.UserAddException;
import com.revature.exception.UserNotFoundException;
import com.revature.model.User;

@Service
public class LoginService {
	
	@Autowired
	private UserRepository userRepository;
	
	public LoginService() throws DatabaseException {
		this.userRepository = new UserRepository();
	}
	
	//This one is for Mockito
	public LoginService(UserRepository userRepository) throws DatabaseException {
		this.userRepository = userRepository;
	}
	
	@Transactional(rollbackFor = {UserNotFoundException.class, BadPasswordException.class})
	public User login(LoginDTO loginDTO) throws BadParameterException, LoginException {
		if(loginDTO.getUsername().isBlank()) {
			throw new BadParameterException("Cannot have blank username or password");
		}
		
		User user = null;
		try {
			user = userRepository.getUserByUsernameAndPassword(loginDTO);
		} catch (UserNotFoundException e) {
			throw new LoginException("User with given username was not found");
		} catch (BadPasswordException e) {
			throw new LoginException("Given password does not match the given username and therefore couldn't be logged in");
		}
		if(user == null) {
			throw new LoginException("No user was able to be returned");
			//This should be handled by the dao layer, but this is just extra error checking
		}
		if(user.getUserID() == -1) {
			throw new LoginException("Username was not found");
		} else if (user.getUserID() == -2) {
			throw new LoginException("Password provided does not match username");
		} else {
			return user;
		}
	}
	
	public UserRepository getUserRepository() {
		return userRepository;
	}
	
	public User createNewUser(PostUserDTO userDTO) throws PasswordHashException, UserAddException {
		User user = new User(userDTO);
		try {
			user = userRepository.addUser(user);
			return user;
		} catch (DatabaseException e) {
			throw new UserAddException(e.getMessage());
		}
	}

}
