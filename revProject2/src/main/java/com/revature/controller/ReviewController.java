package com.revature.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.revature.annotations.LoggedInOnly;
import com.revature.annotations.UnbannedOnly;
import com.revature.dto.DisplayReview;
import com.revature.dto.MessageDTO;
import com.revature.dto.PostReviewDTO;
import com.revature.exception.BadParameterException;
import com.revature.exception.BadPasswordException;
import com.revature.exception.DatabaseException;
import com.revature.exception.EmptyParameterException;
import com.revature.exception.PasswordHashException;
import com.revature.exception.ReviewAddException;
import com.revature.exception.ReviewNotFoundException;
import com.revature.exception.UserNotFoundException;
import com.revature.model.Review;
import com.revature.model.User;
import com.revature.service.LoginService;
import com.revature.service.ReviewService;

@CrossOrigin(allowCredentials = "true", origins = "http://localhost:4200")

@Controller // This is a stereotype annotation, just like @Component, @Service, @Repository
// What those annotations are for, is to have Spring register it as a Spring Bean
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	@GetMapping(path = "review/{id}")
	@LoggedInOnly
	public ResponseEntity<Object> getReviewById(@PathVariable("id") String id) {
		
		/*
		 * The code below seems like a cross cutting concern
		 * We probably want to check if a user is logged in on MANY different endpoints
		 * In order for them to access that data
		 * So we can create an aspect w/ a piece of advice inside of that aspect
		 * That will control whether or not the user has access to that endpoint method
		 */
//		HttpSession session = request.getSession(false); // false because we don't want a new session
//		// to be created if the session does not exist
//		if (session == null) {
//			return ResponseEntity.status(401).body(new MessageDTO("User is not logged in!"));
//		} else {
//			System.out.println(session.getAttribute("loggedInUser"));
//		}
		
		try {
			Review review = reviewService.getReviewByID(id);
			return ResponseEntity.status(200).body(review);
		} catch (ReviewNotFoundException e) {
			return ResponseEntity.status(404).body(new MessageDTO("Review with id " + id + " was not found"));
		} catch (BadParameterException e) {
			return ResponseEntity.status(400).body(new MessageDTO("User provided a bad parameter"));
		} catch (EmptyParameterException e) {
			return ResponseEntity.status(400).body(new MessageDTO("User provided an empty parameter"));
		}
		
	}
	
	@PostMapping(path = "review")
	@LoggedInOnly
	@UnbannedOnly
	public ResponseEntity<Object> addReview(@RequestBody PostReviewDTO reviewDTO) {
		
		try {
			System.out.println(reviewDTO);
			Review review;
			HttpSession session = request.getSession(true);
			reviewDTO.setUser((User)session.getAttribute("currentlyLoggedInUser"));
			DisplayReview dispReview = new DisplayReview(reviewService.postNewReview(reviewDTO));
			return ResponseEntity.status(201).body(dispReview);
		} catch (ReviewAddException e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(new MessageDTO("Unable to create review in the database!"));
		} catch (BadParameterException e) {
			return ResponseEntity.status(400).body(new MessageDTO("User provided a bad parameter"));
		}
		
	}
	
	@GetMapping(path = "game/{id}")
	public ResponseEntity<Object> getReviewsByGame(@PathVariable("id") String gameID){
		try {
			ArrayList<Review> reviewList = null;
			reviewList = reviewService.getReviewsByGame(gameID);
			return ResponseEntity.status(200).body(reviewList);
		} catch (ReviewNotFoundException e) {
			return ResponseEntity.status(404).body(new MessageDTO("No Reviews could be found"));
		} catch (BadParameterException e) {
			return ResponseEntity.status(400).body(new MessageDTO("ID provided was not in correct format"));
		} catch (EmptyParameterException e) {
			return ResponseEntity.status(400).body(new MessageDTO("ID provided was not in correct format"));
		}
	}
	
	@GetMapping(path = "user/{username}")
	public ResponseEntity<Object> getReviewsByUser(@PathVariable("username") String username){
		try {
			ArrayList<Review> reviewList = null;
			reviewList = (ArrayList<Review>) reviewService.getReviewsByUser(username);
			return ResponseEntity.status(200).body(reviewList);
		} catch (ReviewNotFoundException e) {
			return ResponseEntity.status(404).body(new MessageDTO("No Reviews could be found"));
		} catch (EmptyParameterException e) {
			return ResponseEntity.status(400).body(new MessageDTO("Username was left blank"));
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(404).body(new MessageDTO("User was not found"));
		}
	}
	
	@GetMapping(path = "recent")
	public ResponseEntity<Object> getTenMostRecentReviews() {
		return null;
	}
}
