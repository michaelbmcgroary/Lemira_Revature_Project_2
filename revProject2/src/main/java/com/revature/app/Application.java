package com.revature.app;

import com.revature.exception.PasswordHashException;
import com.revature.model.User;
import com.revature.model.UserStatus;
import com.revature.model.UserType;

public class Application {

	
	public static void main(String[] args) throws PasswordHashException {
		
		User user = new User(1, "Username", "Password123*", "George", "Lucas", "glucas@gmail.com", new UserType(1), new UserStatus(1));
		System.out.println(user.getPassword());
		
	}

}
