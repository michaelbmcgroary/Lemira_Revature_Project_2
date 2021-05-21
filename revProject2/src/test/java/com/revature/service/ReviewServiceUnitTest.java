package com.revature.service;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dao.ReviewRepository;
import com.revature.dao.UserRepository;
import com.revature.dto.LoginDTO;
import com.revature.dto.PostReviewDTO;
import com.revature.dto.PostUserDTO;
import com.revature.exception.*;
import com.revature.model.Review;
import com.revature.model.ReviewStatus;
import com.revature.model.User;
import com.revature.model.UserStatus;
import com.revature.model.UserType;
import com.revature.service.LoginService;

@ExtendWith(MockitoExtension.class)
class ReviewTest {

	@InjectMocks

	private static UserRepository mockUserRepository;
	private static ReviewRepository mockReviewRepo;

	@Autowired
	private ReviewService reviewService;

	@BeforeAll
	public static void setUp()  {
		mockUserRepository = mock(UserRepository.class);
		mockReviewRepo = mock(ReviewRepository.class);

		LoginDTO login = new LoginDTO("Username", "Password");
		LoginDTO login2 = new LoginDTO("Username2", "Password");
		User poster = new User(1, "Username", "Password", "First", "Last", "GLucas@gmail.com", new UserType(1), new UserStatus(1));
		PostReviewDTO reviewDTO = new PostReviewDTO(poster, 10, "Test", 12.5, new ReviewStatus(3), 515);
		Review sendReview = new Review(reviewDTO);
		Review retReview = new Review(poster, 10, "Test", 12.5, new ReviewStatus(3), 523);
		ArrayList<Review> reviewList = new ArrayList<Review>();
		reviewList.add(sendReview);
		reviewList.add(retReview);

		when(mockReviewRepo.newReview(sendReview)).thenReturn(retReview);
		
		when(mockReviewRepo.newReview(retReview))
				.thenThrow(new DatabaseException("Bad Review"));

		when(mockReviewRepo.getReviewByID(1)).thenReturn(retReview);

		when(mockReviewRepo.getReviewByID(2))
				.thenThrow(new DatabaseException());

		when(mockReviewRepo.getReviewByID(login, 2)).thenThrow(new NotFinanceManagerException());

		when(mockReviewRepo.getAllRequests(login)).thenReturn(reviewList);

		when(mockReviewRepo.getAllRequestsFilterByStatus(login, new ReviewStatus(1))).thenReturn(reviewList);

		when(mockReviewRepo.getAllRequestsFilterByStatus(login, new ReviewStatus(3))).thenReturn(reviewList);

		when(mockReviewRepo.getPreviousRequestsByEmployee(login)).thenReturn(reviewList);

		when(mockReviewRepo.approveReviewRequest(login, retReview)).thenReturn(goodReturnApproved);

		when(mockReviewRepo.approveReviewRequest(login2, retReview)).thenReturn(goodReturnDenied);

		when(mockReviewRepo.getRecieptByID(eq(login), eq(1))).thenReturn(blob);

		when(mockReviewRepo.getRecieptByID(eq(login), eq(0))).thenThrow(new NoRecieptException());

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

	@Test
	public void test_getReviewsByStatus_NoIssue() {

		User poster = new User(1, "Username", "Password", "First", "Last", "email", new UserRole(2));
		Review review1 = new Review(1, 100.00, "Test", null, poster, null, new ReviewStatus(3), new ReviewType(1), null,
				null);
		PostReviewDTO reviewDTO = new PostReviewDTO("100", "Test", 1, null);
		Review review2 = new Review(reviewDTO);
		review2.setAmount(100.00);
		ArrayList<Review> expected = new ArrayList<Review>();
		expected.add(review1);
		expected.add(review2);
		LoginDTO login = new LoginDTO("Username", "Password");
		ArrayList<Review> actual = (ArrayList<Review>) reviewService.getReviewsByStatus(login, "Approved");
		assertEquals(expected, actual);

	}

	@Test
	public void test_getReviewsByStatus_BlankStatus() {

		try {
			LoginDTO login = new LoginDTO("Username", "Password");
			reviewService.getReviewsByStatus(login, "");
			fail("EmptyParameterException was not thrown");
		} catch (EmptyParameterException e) {
			assertEquals(e.getMessage(), "The status to filter by was left blank");
		}

	}

	@Test
	public void test_getReviewsByStatus_BadStatus() {

		User poster = new User(1, "Username", "Password", "First", "Last", "email", new UserRole(2));
		Review review1 = new Review(1, 100.00, "Test", null, poster, null, new ReviewStatus(3), new ReviewType(1), null,
				null);
		PostReviewDTO reviewDTO = new PostReviewDTO("100", "Test", 1, null);
		Review review2 = new Review(reviewDTO);
		review2.setAmount(100.00);
		ArrayList<Review> expected = new ArrayList<Review>();
		expected.add(review1);
		expected.add(review2);
		LoginDTO login = new LoginDTO("Username", "Password");
		ArrayList<Review> actual = (ArrayList<Review>) reviewService.getReviewsByStatus(login, "Pending");
		assertEquals(expected, actual);
	}

	@Test
	public void test_getReviewsByUser_NoIssue() throws PasswordHashException {
		User poster = new User(1, "Username", "Password", "First", "Last", "GLucas@gmail.com", new UserType(1), new UserStatus(1));
		PostReviewDTO reviewDTO = new PostReviewDTO(poster, 10, "Test", 12.5, new ReviewStatus(3), 515);
		Review sendReview = new Review(reviewDTO);
		Review retReview = new Review(poster, 10, "Test", 12.5, new ReviewStatus(3), 523);
		ArrayList<Review> expected = new ArrayList<Review>();
		expected.add(sendReview);
		expected.add(retReview);
		ArrayList<Review> actual = (ArrayList<Review>) reviewService.getReviewsByUser();
		assertEquals(expected, actual);
	}

	
}
