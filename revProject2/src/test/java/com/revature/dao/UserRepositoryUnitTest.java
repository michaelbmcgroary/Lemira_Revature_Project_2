package com.revature.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import com.revature.dto.LoginDTO;
import com.revature.exception.BadPasswordException;
import com.revature.exception.DatabaseException;
import com.revature.exception.PasswordHashException;
import com.revature.exception.UserNotFoundException;
import com.revature.model.ReviewStatus;
import com.revature.model.User;
import com.revature.model.UserStatus;
import com.revature.model.UserType;


@ExtendWith(SpringExtension.class)
@ContextHierarchy({
	@ContextConfiguration("classpath:applicationContext.xml"),
	@ContextConfiguration("classpath:dispatcherContext.xml")
})
@WebAppConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryUnitTest {
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	private UserRepository userRepository;

	
	@BeforeEach
	@Transactional
	@Commit
	void setUp() throws PasswordHashException {
		Session session = sessionFactory.getCurrentSession();
		
		session.persist(new UserStatus(1));
		session.persist(new UserStatus(2));
		session.persist(new UserType(1));
		session.persist(new UserType(2));
		session.persist(new ReviewStatus(1));
		session.persist(new ReviewStatus(2));
		session.persist(new ReviewStatus(3));
		session.persist(new ReviewStatus(4));
		
	}
	
	
	@Test
	@Transactional
	@Order(0)
	@Commit
	void test_addUser_happy() throws PasswordHashException, DatabaseException {
		User user = new User(0, "Username3", "Password", "First", "Last", "G2Lucas@gmail.com", sessionFactory.getCurrentSession().get(UserType.class, 1), sessionFactory.getCurrentSession().get(UserStatus.class, 1));
		User newUser = userRepository.addUser(user);
		assertEquals(newUser.getUserID(), 2);
	}
	
	@Test
	@Transactional
	@Order(1)
	@Commit
	void test_addUser_userAlreadyExists() throws PasswordHashException, DatabaseException {
		try {
			User newUser = userRepository.addUser(sessionFactory.getCurrentSession().get(User.class, 1));
			newUser.getUserID();
			fail("javax.persistence.PersistenceException was not thrown");
		} catch (DatabaseException e) {
			assertEquals(e.getMessage(), "User could not be added because unique value already existed.");
		}
	}
	
	@Test
	@Transactional
	@Order(2)
	void test_getUserByUsernameAndPassword_happy() throws UserNotFoundException, BadPasswordException, PasswordHashException {
		LoginDTO loginDTO = new LoginDTO("Username3","Password");
		User user = userRepository.getUserByUsernameAndPassword(loginDTO);
		assertEquals(user.getUsername(), "Username3");
	}
	
	@Test
	@Transactional
	@Order(3)
	void test_getUserByUsernameAndPassword_PasswordDoesNotMatch() throws PasswordHashException, UserNotFoundException, BadPasswordException {
		try {
			LoginDTO loginDTO = new LoginDTO("Username3","Password3");
			 userRepository.getUserByUsernameAndPassword(loginDTO);
			fail("BadPasswordException not thrown");
		} catch (BadPasswordException e) {
			assertEquals(e.getMessage(), "Password does not match username");
		}
		
	}
	
	
 
//	@Test
//	@Transactional
//	
//	@Order(4)
//	void test_isModerator_happy() throws PasswordHashException, DatabaseException {
//	User user = sessionFactory.getCurrentSession().get(User.class, 1);
//	LoginDTO loginDTO = new LoginDTO("Username3","Password");
//		userRepository.isModerator(loginDTO);
//	}
	
	@Test
	@Transactional
	@Order(5)
	void test_getUserIDByUsername_happy() throws DatabaseException {
		String username = "Username3";
		int id = userRepository.getUserIDByUsername(username);
		
		assertEquals(id, 2);
	}
	
	@Test
	@Transactional
	@Order(6)
	void test_getUserIDByUsername_UserDoesNotExist() {
		try {
			String username = "UsernameRay";
			int id = userRepository.getUserIDByUsername(username);
		} catch (DatabaseException e) {
			String username = "UsernameRay";
			assertEquals(e.getMessage(), "User with username " + username + " does not exist");
		}
	}

}