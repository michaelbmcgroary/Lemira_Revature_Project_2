package com.revature.dao;

import static org.junit.jupiter.api.Assertions.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import com.revature.exception.DatabaseException;
import com.revature.exception.PasswordHashException;
import com.revature.model.Review;
import com.revature.model.User;
import com.revature.model.UserStatus;
import com.revature.model.UserType;
import com.revature.model.ReviewStatus;

@ExtendWith(SpringExtension.class)
@ContextHierarchy({
	@ContextConfiguration("classpath:applicationContext.xml"),
	@ContextConfiguration("classpath:dispatcherContext.xml")
})
@WebAppConfiguration
@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReviewRepositoryUnitTest {
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	
	@Test
	@Transactional
	@Order(2)
	@Commit
	void test_newReview_happy() throws PasswordHashException, DatabaseException {
		Review review = new Review(sessionFactory.getCurrentSession().get(User.class, 1), 10, "description", 5, sessionFactory.getCurrentSession().get(ReviewStatus.class, 1), 123);
		Review newReview = reviewRepository.newReview(review);
		assertTrue(newReview.getReviewID() == 1);
	}

	//The database exceptions are something we cannot test for because they are a way to error check for freak incidents
	//that we have no way to predict

	@Test
	@Transactional
	@Order(3)
	@Commit
	void test_getReviewById_NoIssue() throws DatabaseException {
		Review actual = reviewRepository.getReviewByID(1);
		assertEquals(1, actual.getReviewID());
	}

	
	@Test
	@Transactional
	@Order(3)
	@Commit
	void test_getReviewById_reviewDoesNotExist() throws DatabaseException {
		try {
			reviewRepository.getReviewByID(Integer.MAX_VALUE);
			fail("DatabaseException was not thrown");
		} catch (DatabaseException e) {
			assertEquals(e.getMessage(), null);
		}
	}
	
	
	@Test
	@Transactional
	@Commit 
	@Order(4)
	void test_getAllReviews_happy() throws DatabaseException {
		List<Review> actual = reviewRepository.getAllReviews();
		assertTrue(actual.size() != 0);
	}
	
	
	@Test
	@Transactional
	@Commit
	@Order(1)
	void test_getAllReviews_reviewsNotFound() throws DatabaseException, PasswordHashException {
		List<Review> actual = reviewRepository.getAllReviews();
		assertEquals(actual.size(), 0);
	}

	@Test
	@Transactional
	@Commit
	@Order(4)
	void test_getReviewsByUser_happy() throws DatabaseException {
		List<Review> actual = reviewRepository.getReviewsByUser("Username");
		assertEquals(actual.size(), 1);
	}
	
	@Test
	@Transactional
	@Commit 
	@Order(5)
	void test_getReviewsByUser_userDoesNotExist() throws DatabaseException {
		try {
			reviewRepository.getReviewsByUser("Username2");
			fail("DatabaseException not thrown");
		} catch (DatabaseException e) {
			assertEquals(e.getMessage(), "User with username Username2 does not exist");
		}
	}
	
	
	@Test
	@Transactional
	@Commit 
	@Order(0)
	void test_getReviewsByUser_reviewsNotFound() throws DatabaseException, PasswordHashException {
		Session session = sessionFactory.getCurrentSession();
		session.persist(new UserStatus(1));
		session.persist(new UserStatus(2));
		session.persist(new UserType(1));
		session.persist(new UserType(2));
		session.persist(new ReviewStatus(1));
		session.persist(new ReviewStatus(2));
		session.persist(new ReviewStatus(3));
		session.persist(new ReviewStatus(4));
		User user = new User(0, "Username", "Password", "First", "Last", "GLucas@gmail.com", session.get(UserType.class, 1), session.get(UserStatus.class, 1));
		session.persist(user);
		
		try {
			List<Review> list = reviewRepository.getReviewsByUser(user.getUsername());
			assertEquals(list.size(), 0);
		} catch (DatabaseException e) {
			assertEquals(e.getMessage(), "User with username Username does not exist");
		}
	}
	
	
	@Test
	@Transactional
	@Commit 
	@Order(3)
	void test_getReviewsByGame_happy() throws DatabaseException {
		List<Review> actual = reviewRepository.getReviewsByGame(123);
		assertTrue(actual.size() == 1);
	}
	
	
	@Test
	@Transactional
	@Commit 
	@Order(1)
	void test_getReviewsByGame_reviewsNotFound() throws DatabaseException {
		try {
			List<Review> actual = reviewRepository.getReviewsByGame(123);;
			assertTrue(actual.size() == 0);
		} catch (DatabaseException e) {
			assertEquals(e.getMessage(), "No Reviews exist for this game");
		}
	}
	

}