package com.revature.service;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
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
	
	
	public Review postNewReview(PostReviewDTO reviewDTO) throws ReviewAddException {
		try {
			Review review = new Review(reviewDTO);
			review = reviewRepo.newReview(review);
			System.out.println(review);
			return review;
		} catch (DatabaseException e) {
			throw new ReviewAddException(e.getMessage());
		}
	}
	
	public Review getReviewByID(String id) throws ReviewNotFoundException, BadParameterException, EmptyParameterException {
		Review review = null;
		try {
			if(id.trim().equals("")) {
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
	
	
	public List<Review> getAllReviews() throws ReviewNotFoundException {
		List<Review> reviewList = null;
		try {
			reviewList = reviewRepo.getAllReviews();
			return reviewList;
		} catch (DatabaseException e) {
			throw new ReviewNotFoundException("No Reviews could be found");
		}
	}
	

	
	public List<Review> getReviewsByUser(String username) throws ReviewNotFoundException, EmptyParameterException, UserNotFoundException{
		List<Review> reviewList = null;
		try {
			if(username.trim().equals("")) {
				throw new EmptyParameterException("The username of the user was left blank");
			}
			reviewList = reviewRepo.getReviewsByUser(username);
			return reviewList;
		} catch (DatabaseException e) {
			throw new ReviewNotFoundException("No Reviews could be found");
		}
	}

	public ArrayList<Review> getReviewsByGame(String id) throws ReviewNotFoundException, BadParameterException, EmptyParameterException {
		List<Review> reviewList = null;
		try {
			if(id.trim().equals("")) {
				throw new EmptyParameterException("No id was provided for the game");
			}
			int gameID = Integer.parseInt(id);
			reviewList = reviewRepo.getReviewsByGame(gameID);
			return (ArrayList<Review>) reviewList;
		} catch (DatabaseException e) {
			throw new ReviewNotFoundException("No Reviews could be found");
		} catch (NumberFormatException e) {
			throw new BadParameterException("Game ID must be a number value");
		}
	}
	

}
