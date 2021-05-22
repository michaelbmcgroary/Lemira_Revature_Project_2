package com.revature.controller;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.eq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.dao.UserRepository;
import com.revature.dto.DisplayReview;
import com.revature.dto.LoginDTO;
import com.revature.dto.PostReviewDTO;
import com.revature.dto.PostUserDTO;
import com.revature.exception.*;
import com.revature.model.Review;
import com.revature.model.ReviewStatus;
import com.revature.model.User;
import com.revature.model.UserStatus;
import com.revature.model.UserType;
import com.revature.service.ReviewService;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

/*
	
	MockMvc mockMvc;
	
	@InjectMocks
	
	
	private ReviewController reviewController;
	
	@Autowired
	private ReviewService mockReviewService;
	
	
	//private static Logger mockLogger;
	
	@BeforeEach
	public void setUp() throws ReviewNotFoundException, PasswordHashException {
		mockReviewService = mock(ReviewService.class);
		//mockLogger = mock(Logger.class);
		
		User poster = new User(1, "Username", "Password", "First", "Last", "GLucas@gmail.com", new UserType(1), new UserStatus(1));
		PostReviewDTO reviewDTO = new PostReviewDTO(poster, 10, "Test", 12.5, new ReviewStatus(3), 515);
		Review sendReview = new Review(reviewDTO);
		Review retReview = new Review(poster, 10, "Test", 12.5, new ReviewStatus(3), 523);
		ArrayList<Review> reviewList = new ArrayList<Review>();
		reviewList.add(sendReview);
		reviewList.add(retReview);
		
		//Mockito.doNothing().when(mockLogger);

		when(mockReviewService.getAllReviews()).thenReturn(reviewList);
		
		this.mockMvc = MockMvcBuilders.standaloneSetup(mockReviewService).build();
		

	}
	
	@BeforeEach
	public void beforeTest() {
		reviewController = new ReviewController();
	}
*/	
	@Test
	void test() {
		
	}
/*	
	@Test
	void test_getAllReviews_noIssue() throws PasswordHashException {
		User poster = new User(1, "Username", "Password", "First", "Last", "GLucas@gmail.com", new UserType(1), new UserStatus(1));
		PostReviewDTO reviewDTO = new PostReviewDTO(poster, 10, "Test", 12.5, new ReviewStatus(3), 515);
		Review sendReview = new Review(reviewDTO);
		Review retReview = new Review(poster, 10, "Test", 12.5, new ReviewStatus(3), 523);
		ArrayList<DisplayReview> expected = new ArrayList<DisplayReview>();
		expected.add(new DisplayReview(sendReview));
		expected.add(new DisplayReview(retReview));
		
		ResponseEntity<Object> actual = reviewController.getAllReviews();
		assertEquals(ResponseEntity.status(200).body(expected), actual);
	}
*/
}
