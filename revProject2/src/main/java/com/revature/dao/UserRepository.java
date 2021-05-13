package com.revature.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.revature.dto.LoginDTO;
import com.revature.exception.BadPasswordException;
import com.revature.exception.DatabaseException;
import com.revature.exception.UserNotFoundException;
import com.revature.model.User;
import com.revature.model.UserStatus;
import com.revature.model.UserType;

@Repository
public class UserRepository {

	@Autowired
	private SessionFactory sessionFactory;
	
	private Logger logger = LoggerFactory.getLogger(UserRepository.class);
	
	//add in fake data for now and replace with SQL later
	List<User> users = new ArrayList<User>();
	
	
	
	public UserRepository() throws DatabaseException {
		
	}
	
	
	public User getUserByUsernameAndPassword(LoginDTO loginDTO) throws UserNotFoundException, BadPasswordException {
		//if user is not found, return a user with an ID of -1 and null values for the rest
		//if the password is not correct, return a user with an ID of -2 and null values for the rest
		
		try {
			Session session = sessionFactory.getCurrentSession();
			@SuppressWarnings("rawtypes")
			Query query = session.createQuery("from User u WHERE u.username = :un AND u.password = :pw");
			query.setParameter("un", loginDTO.getUsername());
			query.setParameter("pw", loginDTO.getPassword());
			User retUser = (User) query.getSingleResult();
			return retUser;
		} catch (javax.persistence.NoResultException e) {
			try {
				Session session = sessionFactory.getCurrentSession();
				@SuppressWarnings("rawtypes")
				Query query = session.createQuery("from User u WHERE u.username = :un");
				query.setParameter("un", loginDTO.getUsername());
				throw new BadPasswordException("Password does not match username");
			} catch (javax.persistence.NoResultException e1) {
				throw new UserNotFoundException("User could not be found.");
			} 
		} 
	}
	
	@Transactional
	public User addUser(User user) throws DatabaseException {
		//may replace user with a DTO that doesn't include the id
		try {
			Session session = sessionFactory.getCurrentSession();
			//session.getTransaction().begin();
			user.setUserType(new UserType(1));
			user.setUserStatus(new UserStatus(1));
			//session.getTransaction().commit();
			user.setUserID(0);
			session.persist(user);		
			@SuppressWarnings("rawtypes")
			Query query = session.createQuery("from User u WHERE u.username = :un");
			query.setParameter("un", user.getUsername());
			User retUser = (User) query.getSingleResult();
			return retUser;
		} catch (javax.persistence.NoResultException e) {
			throw new DatabaseException("User could not be added. Exception message is: " + e.getMessage());
		} catch (javax.persistence.PersistenceException e) {
			throw new DatabaseException("User could not be added because unique value already existed.");
			//may add in some checking to see what constraint it is and send back a specific Exception to display to the user
			//like "username/email already exists"
		}
	}
	
	/*
	public User promoteEmployeeToModerator(LoginDTO login, int UserToPromoteID) {
		return null;
	}
	*/
	
	public boolean isModerator(LoginDTO loginDTO) throws DatabaseException {
		try {
			Session session = sessionFactory.getCurrentSession();
			@SuppressWarnings("rawtypes")
			Query query = session.createQuery("select userType from User u WHERE u.username = :un AND u.password = :pw");
			query.setParameter("un", loginDTO.getUsername());
			query.setParameter("pw", loginDTO.getPassword());
			UserType type = (UserType) query.getSingleResult();
			if(type.getTypeID() == 2) {
				return true;
			} else {
				return false;
			}
		} catch (javax.persistence.NoResultException e) {
			throw new DatabaseException("When checking if user was a moderator, user could not be found.");
		} 
		
	}


	public List<User> getAllUsers() {
		Session session = sessionFactory.getCurrentSession();
		List<User> userList = session.createQuery("from User", User.class).getResultList();
		return userList;
	}
	
	
	public int getUserIDByUsername(String username) throws DatabaseException {
		try {
			Session session = sessionFactory.getCurrentSession();
			@SuppressWarnings("rawtypes")
			Query query = session.createQuery("select userID from User u WHERE u.username = :un");
			query.setParameter("un", username);
			int ret = (Integer) query.getSingleResult();
			return ret;
		} catch (javax.persistence.NoResultException e) {
			throw new DatabaseException("User with username " + username + " does not exist");
		}
		
	}
	
}
