package com.revature.dao;

import static org.junit.jupiter.api.Assertions.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import com.revature.dao.ReviewRepository;
import com.revature.dto.PostReviewDTO;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReviewRepositoryUnitTest {
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	private ReviewRepository reviewRepository;

	
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
		session.persist(new User(0, "Username", "Password", "First", "Last", "GLucas@gmail.com", session.get(UserType.class, 1), session.get(UserStatus.class, 1)));
	}
	
	
	@Test
	@Transactional
	@Order(0)
	@Commit
	void test_newReview_happy() throws PasswordHashException, DatabaseException {
		Review review = new Review(sessionFactory.getCurrentSession().get(User.class, 1), 10, "description", 5, sessionFactory.getCurrentSession().get(ReviewStatus.class, 1), 123);
		Review newReview = reviewRepository.newReview(review);
		assertTrue(newReview.getReviewID()!=0);
		
		
	}
	
	

}