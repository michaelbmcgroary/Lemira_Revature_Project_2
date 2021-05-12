package com.revature.dto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.revature.exception.PasswordHashException;

public class LoginDTO {

	private String username;
	private String password;
	
	public LoginDTO() {
		super();
	}
	
	public LoginDTO(String username, String password) throws PasswordHashException {
		this.username = username;
		this.password = hashPassword(password, 1);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws PasswordHashException {
		this.password = hashPassword(password, 2);
	}
	
	private String hashPassword(String password, int comingFrom) throws PasswordHashException {
		String hashedPassword;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
	        md.update(password.getBytes());
	        byte[] bytes = md.digest(password.getBytes());
	        StringBuilder sb = new StringBuilder();
	        for(int i=0; i< bytes.length ;i++) {
	        	sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        hashedPassword = sb.toString();
	    } catch (NoSuchAlgorithmException e) {
	        switch(comingFrom) {
	        	case 1: throw new PasswordHashException("There was an error when hashing the password upon the creation of the LoginDTO");
	        	case 2: throw new PasswordHashException("There was an error when hashing the password upon setting the password after LoginDTO's creation");
	        	default: throw new PasswordHashException("There was an error when hashing the password and method was called from unknown source");
	        }
	    }
		return hashedPassword;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoginDTO other = (LoginDTO) obj;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LoginDTO [username=" + username + ", password=" + password + "]";
	}
	
	
}
