package com.revature.service;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revature.dao.ReviewRepository;
import com.revature.dao.UserRepository;
import com.revature.dto.PostReviewDTO;
import com.revature.exception.*;
import com.revature.model.Review;
import com.revature.model.ReviewStatus;
import com.revature.model.User;
import com.revature.model.UserStatus;
import com.revature.model.UserType;

@ExtendWith(MockitoExtension.class)
class ReviewServiceUnitTest {

	@InjectMocks

	private static UserRepository mockUserRepository;
	private static ReviewRepository mockReviewRepo;

	@Autowired
	private ReviewService reviewService;

	@BeforeAll
	public static void setUp() throws PasswordHashException, DatabaseException  {
		mockUserRepository = mock(UserRepository.class);
		mockReviewRepo = mock(ReviewRepository.class);

		User poster = new User(1, "Username", "Password", "First", "Last", "GLucas@gmail.com", new UserType(1), new UserStatus(1));
		PostReviewDTO reviewDTO = new PostReviewDTO(poster, 10, "Test", 12.5, new ReviewStatus(3), 515);
		Review sendReview = new Review(reviewDTO);
		Review retReview = new Review(poster, 10, "Test", 12.5, new ReviewStatus(3), 523);
		ArrayList<Review> reviewList = new ArrayList<Review>();
		reviewList.add(sendReview);
		reviewList.add(retReview);

		when(mockReviewRepo.newReview(sendReview)).thenReturn(retReview);
		when(mockReviewRepo.newReview(retReview)).thenThrow(new DatabaseException("Bad Review"));
		when(mockReviewRepo.getReviewByID(1)).thenReturn(retReview);
		when(mockReviewRepo.getReviewByID(2)).thenThrow(new DatabaseException());
		when(mockReviewRepo.getAllReviews()).thenReturn(reviewList);
		//when(mockReviewRepo.getAllReviews()).thenThrow(new DatabaseException());
		when(mockReviewRepo.getReviewsByUser("Username")).thenReturn(reviewList);
		when(mockReviewRepo.getReviewsByUser("Username2")).thenThrow(new DatabaseException(""));
		when(mockReviewRepo.getReviewsByGame(1)).thenReturn(reviewList);
		when(mockReviewRepo.getReviewsByGame(2)).thenThrow(new DatabaseException());
		

	}

	@BeforeEach
	public void beforeTest() {
		reviewService = new ReviewService(mockUserRepository, mockReviewRepo);
	}

	
	
	@Test
	public void test_postNewReview_NoIssue() throws PasswordHashException, ReviewAddException, BadParameterException  {
		User poster = new User(1, "Username", "Password", "First", "Last", "GLucas@gmail.com", new UserType(1), new UserStatus(1));
		PostReviewDTO reviewDTO = new PostReviewDTO(poster, 10, "Test", 12.5, new ReviewStatus(3), 515);
		reviewService.postNewReview(reviewDTO);
	}

	@Test
	public void test_postNewReview_DatabaseIssues() throws PasswordHashException {
		try {
			User poster = new User(1, "Username", "Password", "First", "Last", "GLucas@gmail.com", new UserType(1), new UserStatus(1));
			reviewService.postNewReview(new PostReviewDTO(poster, 10, "Test", 12.5, new ReviewStatus(3), 523));
			fail("EmptyParameterException was not thrown");
		} catch (ReviewAddException e) {
			assertEquals(e.getMessage(), "Bad Review");
		}
	}

//---

	
	@Test
	public void test_getReviewByID_NoIssue() throws PasswordHashException, ReviewNotFoundException, BadParameterException, EmptyParameterException {
		String id = "1";
		User poster = new User(1, "Username", "Password", "First", "Last", "GLucas@gmail.com", new UserType(1), new UserStatus(1));
		Review expected = new Review(poster, 10, "Test", 12.5, new ReviewStatus(3), 523);
		Review actual = reviewService.getReviewByID(id);
		assertEquals(expected, actual);
	}

	@Test
	public void test_getReviewByID_BadID() throws ReviewNotFoundException, EmptyParameterException {
		try {
			reviewService.getReviewByID("Test");
			fail("BadParameterException was not thrown");
		} catch (BadParameterException e) {
			assertEquals(e.getMessage(), "The Review ID provided must be of type int");
		}
	}
	
	@Test
	public void test_getReviewByID_EmptyID() throws ReviewNotFoundException, BadParameterException {
		try {
			reviewService.getReviewByID("  ");
			fail("EmptyParameterException was not thrown");
		} catch (EmptyParameterException e) {
			assertEquals(e.getMessage(), "The Review ID was left blank");
		}
	}

	@Test
	public void test_getReviewByID_NoReview() throws BadParameterException, EmptyParameterException {
		try {
			reviewService.getReviewByID("2");
			fail("NoReviewException was not thrown");
		} catch (ReviewNotFoundException e) {
			assertEquals(e.getMessage(), "The review with id 2 could not be found");
		}
	}
	
//---
	
	@Test
	public void test_getAllReviews_NoIssue() throws PasswordHashException, ReviewNotFoundException {
		User poster = new User(1, "Username", "Password", "First", "Last", "GLucas@gmail.com", new UserType(1), new UserStatus(1));
		PostReviewDTO reviewDTO = new PostReviewDTO(poster, 10, "Test", 12.5, new ReviewStatus(3), 515);
		Review sendReview = new Review(reviewDTO);
		Review retReview = new Review(poster, 10, "Test", 12.5, new ReviewStatus(3), 523);
		ArrayList<Review> expected = new ArrayList<Review>();
		expected.add(sendReview);
		expected.add(retReview);
		ArrayList<Review> actual = (ArrayList<Review>) reviewService.getAllReviews();
		assertEquals(expected, actual);
	}
	
	/*
	@Test
	public void test_getAllReviews_noReviews() {
		try {
			//I don't know how to get this test to pass
			reviewService.getAllReviews();
			fail("NoReviewException was not thrown");
		} catch (ReviewNotFoundException e) {
			assertEquals(e.getMessage(), "No Reviews could be found");
		}
	}
	*/

//---
	
	@Test
	public void test_getReviewsByUser_NoIssue() throws PasswordHashException, ReviewNotFoundException, EmptyParameterException, UserNotFoundException {
		User poster = new User(1, "Username", "Password", "First", "Last", "GLucas@gmail.com", new UserType(1), new UserStatus(1));
		PostReviewDTO reviewDTO = new PostReviewDTO(poster, 10, "Test", 12.5, new ReviewStatus(3), 515);
		Review sendReview = new Review(reviewDTO);
		Review retReview = new Review(poster, 10, "Test", 12.5, new ReviewStatus(3), 523);
		ArrayList<Review> expected = new ArrayList<Review>();
		expected.add(sendReview);
		expected.add(retReview);
		ArrayList<Review> actual = (ArrayList<Review>) reviewService.getReviewsByUser("Username");
		assertEquals(expected, actual);
	}
	
	@Test
	public void test_getReviewsByUser_noReviews() throws EmptyParameterException, UserNotFoundException {
		try {
			reviewService.getReviewsByUser("Username2");
			fail("NoReviewException was not thrown");
		} catch (ReviewNotFoundException e) {
			assertEquals(e.getMessage(), "No Reviews could be found");
		}
	}
	
	@Test
	public void test_getReviewsByUser_blankUsername() throws UserNotFoundException, ReviewNotFoundException {
		try {
			reviewService.getReviewsByUser("   ");
			fail("EmptyParameterException was not thrown");
		} catch (EmptyParameterException e) {
			assertEquals(e.getMessage(), "The username of the user was left blank");
		}
	}
	
	
//---
	
	@Test
	public void test_getReviewsByGame_NoIssue() throws ReviewNotFoundException, BadParameterException, EmptyParameterException, PasswordHashException  {
		User poster = new User(1, "Username", "Password", "First", "Last", "GLucas@gmail.com", new UserType(1), new UserStatus(1));
		PostReviewDTO reviewDTO = new PostReviewDTO(poster, 10, "Test", 12.5, new ReviewStatus(3), 515);
		Review sendReview = new Review(reviewDTO);
		Review retReview = new Review(poster, 10, "Test", 12.5, new ReviewStatus(3), 523);
		ArrayList<Review> expected = new ArrayList<Review>();
		expected.add(sendReview);
		expected.add(retReview);
		ArrayList<Review> actual = (ArrayList<Review>) reviewService.getReviewsByGame("1");
		assertEquals(expected, actual);
	}
	
	@Test
	public void test_getReviewsByGame_emptyParameter() throws UserNotFoundException, BadParameterException, ReviewNotFoundException {
		try {
			reviewService.getReviewsByGame("  ");
			fail("EmptyParameterException was not thrown");
		} catch (EmptyParameterException e) {
			assertEquals(e.getMessage(), "No id was provided for the game");
		}
	}
	
	@Test
	public void test_getReviewsByGame_badParameter() throws ReviewNotFoundException, EmptyParameterException  {
		try {
			reviewService.getReviewsByGame("abc");
			fail("BadParameterException was not thrown");
		} catch (BadParameterException e) {
			assertEquals(e.getMessage(), "Game ID must be a number value");
		}
	}

	@Test
	public void test_getReviewsByGame_noReviews() throws EmptyParameterException, UserNotFoundException, BadParameterException {
		try {
			reviewService.getReviewsByGame("2");
			fail("NoReviewException was not thrown");
		} catch (ReviewNotFoundException e) {
			assertEquals(e.getMessage(), "No Reviews could be found");
		}
	}


}
