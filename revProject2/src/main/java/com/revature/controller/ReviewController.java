package com.revature.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.revature.annotations.LoggedInOnly;
import com.revature.dto.MessageDTO;
import com.revature.dto.PostReviewDTO;
import com.revature.exception.BadParameterException;
import com.revature.exception.DatabaseException;
import com.revature.exception.EmptyParameterException;
import com.revature.exception.ReviewAddException;
import com.revature.exception.ReviewNotFoundException;
import com.revature.model.Review;
import com.revature.service.LoginService;
import com.revature.service.ReviewService;

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
	public ResponseEntity<Object> addReview(@RequestBody PostReviewDTO reviewDTO) {
		
		try {
			Review review;

			review = reviewService.postNewReview(reviewDTO);
			
			return ResponseEntity.status(201).body(review);
		} catch (ReviewAddException e) {
			return ResponseEntity.status(500).body(new MessageDTO("Unable to create review in the database!"));
		} catch (BadParameterException e) {
			return ResponseEntity.status(400).body(new MessageDTO("User provided a bad parameter"));
		}
		
	}
	
}
