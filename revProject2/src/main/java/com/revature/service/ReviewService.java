package com.revature.service;

import java.sql.Blob;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.annotations.LoggedInOnly;
import com.revature.dao.ReviewRepository;
import com.revature.dao.UserRepository;
import com.revature.dto.LoginDTO;
import com.revature.dto.PostReviewDTO;
import com.revature.exception.BadParameterException;
import com.revature.exception.BadPasswordException;
import com.revature.exception.DatabaseException;
import com.revature.exception.EmptyParameterException;
import com.revature.exception.NotModeratorException;
import com.revature.exception.PasswordHashException;
import com.revature.exception.ReviewAddException;
import com.revature.exception.ReviewNotFoundException;
import com.revature.exception.UserNotFoundException;
import com.revature.model.Review;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reviewRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	public ReviewService() throws DatabaseException {
		
	}
	
	//Normally, we don't need this and would use the other one, but for testing, we need this to "inject" the mock object into this service
	public ReviewService(UserRepository userRepo, ReviewRepository reviewRepository) {
		this.reviewRepo = reviewRepository;
	}
	
	@LoggedInOnly
	public Review postNewReview(PostReviewDTO reviewDTO) throws ReviewAddException, BadParameterException {
		//culprit figures out which exception to throw, may add after I figure out the controller layer
		//int culprit = 0;
		try {
			Review review = new Review(reviewDTO);
			//review.setAmount(Double.parseDouble(reviewDTO.getAmount()));
			//keep this function commented out because I need to do string parsing
			review = reviewRepo.newReview(review);
			return review;
		} catch (DatabaseException e) {
			throw new ReviewAddException(e.getMessage());
		} catch (NumberFormatException e) {
			//throw new BadParameterException("User provided an invalid parameter when it was supposed to be a number " + reviewDTO.getAmount());
			//deal with number formatting when parsing strings of the game IDs and ratings
			return null;
			
			
			
			///FIX THIS
		}
	}
	
	public Review getReviewByID(String id) throws ReviewNotFoundException, BadParameterException, EmptyParameterException {
		Review review = null;
		try {
			if(id.isBlank()) {
				throw new EmptyParameterException("The Review ID was left blank");
			}
			int reviewID = Integer.parseInt(id);
			review = reviewRepo.getReviewByID(reviewID);
			return review;
		} catch (DatabaseException e) {
			throw new ReviewNotFoundException("The review with id " + id + " could not be found");
		} catch (NumberFormatException e) {
			throw new BadParameterException("The Review ID provided must be of type int");
		}
	}
	
	
	public List<Review> getAllReviews(LoginDTO login) throws ReviewNotFoundException, EmptyParameterException{
		
		//GET RID OF THE NEED FOR LOGIN
		
		List<Review> reviewList = null;
		try {
			if(login.getUsername().isBlank()) {
				throw new EmptyParameterException("The username of the logged in user was not found");
			}
			if(login.getPassword().isBlank()) {
				throw new EmptyParameterException("The password of the logged in user was not found");
			}
			reviewList = reviewRepo.getAllReviews(login);
			return reviewList;
		} catch (DatabaseException e) {
			throw new ReviewNotFoundException("No Reviews could be found");
		} catch (NotModeratorException e) {
			throw new ReviewNotFoundException("The login credentials are not for a finance manager");
		}
	}
	

	@LoggedInOnly
	public List<Review> getReviewsByUser(String username) throws ReviewNotFoundException, EmptyParameterException, PasswordHashException, BadPasswordException, UserNotFoundException{
		List<Review> reviewList = null;
		try {
			if(username.isBlank()) {
				throw new EmptyParameterException("The username of the user");
			}
			reviewList = reviewRepo.getReviewsByUser(username);
			return reviewList;
		} catch (DatabaseException e) {
			throw new ReviewNotFoundException("No Reviews could be found");
		}
	}
	

}
