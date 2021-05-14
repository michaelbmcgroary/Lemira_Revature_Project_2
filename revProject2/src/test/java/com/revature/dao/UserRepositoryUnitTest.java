package com.revature.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@ContextHierarchy({
	@ContextConfiguration("classpath:applicationContext.xml"),
	@ContextConfiguration("classpath:dispatcherContext.xml")
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebAppConfiguration
class UserRepositoryUnitTest {

	@Autowired
	private UserRepository userRepo;

	@Test
	@Transactional
	@Order(0)
	@Rollback(false)
	void test1() {
		//update the name of the test to how you typically name them (check previous project for reference)
		//create the inputs to give to the repository
		//User user = UserRepo.addUser();
		//assertTrue(user.getID() !=0
		
	}
	
	@Test
	@Transactional
	@Order(1)
	void test2() {
		//get user/review by ID needs to happen after the other test
		//if the same order number is given, it works like JUnit 4 where it's random
		//make sure to add information into the database before testing get
		//Rollback(false) and @Commit mean that the changes won't be thrown out after the test
		//use that to populate and the do a get test
		
		//@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
		//This gets rid of the current context at the end of the method and creates a new database
		//Bach's notes have a bit more on this
		
		//User actual = userRepo.getUserByUsernameAndPassword(null)
		
	}
	

}
