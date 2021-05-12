package com.revature.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.revature.dto.PostUserDTO;
import com.revature.exception.PasswordHashException;



@Entity
@Table(name="glb_users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "userID")
	private int userID;
	
	@Column(name = "username", length=50)
	private String username;
	
	@Column(name = "password", length=50)
	private String password;
	
	@Column(name = "user_firstname", length=100)
	private String firstName;
	
	@Column(name = "user_lastname", length=100)
	private String lastName;
	
	@Column(name = "user_email", length=150)
	private String email;

	@ManyToOne
	@JoinColumn(name = "user_type_id")
	private UserType userType;
	
	@ManyToOne
	@JoinColumn(name = "user_status_id")
	private UserStatus userStatus;
	

	
	public User() {
		super();
	}
	
	public User(int userID, String username, String password, String firstName, String lastName, String email, UserType userType, UserStatus userStatus) throws PasswordHashException {
		super();
		this.userID = userID;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.userType = userType;
		this.userStatus = userStatus;
		this.password = hashPassword(password, 1);
	}
	
	public User(PostUserDTO userDTO) throws PasswordHashException {
		this.username = userDTO.getUsername();
		this.password = hashPassword(userDTO.getPassword(), 1);
		this.firstName = userDTO.getFirstName();
		this.lastName = userDTO.getLastName();
		this.email = userDTO.getEmail();
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
	        	case 1: throw new PasswordHashException("There was an error when hashing the password upon the creation of the User");
	        	case 2: throw new PasswordHashException("There was an error when hashing the password upon setting the password after User's creation");
	        	default: throw new PasswordHashException("There was an error when hashing the password and method was called from unknown source");
	        }
	    }
		return hashedPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws PasswordHashException {
		this.password = hashPassword(password, 2);
	}
	
	public void setPasswordNoHash(String password) {
		this.password = password;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + userID;
		result = prime * result + ((userStatus == null) ? 0 : userStatus.hashCode());
		result = prime * result + ((userType == null) ? 0 : userType.hashCode());
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
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (userID != other.userID)
			return false;
		if (userStatus == null) {
			if (other.userStatus != null)
				return false;
		} else if (!userStatus.equals(other.userStatus))
			return false;
		if (userType == null) {
			if (other.userType != null)
				return false;
		} else if (!userType.equals(other.userType))
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
		return "User [userID=" + userID + ", username=" + username + ", password=" + password + ", firstName="
				+ firstName + ", lastName=" + lastName + ", email=" + email + ", userType=" + userType + ", userStatus="
				+ userStatus + "]";
	}

	
	
}
