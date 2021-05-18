package com.revature.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.revature.annotations.LoggedInOnly;
import com.revature.annotations.ModeratorOnly;
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


//@CrossOrigin(allowCredentials = "true", origins = "*", allowedHeaders = "*")
@CrossOrigin( allowCredentials = "true" ,origins = "http://localhost:4200")
//@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
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
		
		try {
			Review review = reviewService.getReviewByID(id);
			RestTemplate template = new RestTemplate();
			RequestEntity<String> request = RequestEntity
				     .post("https://api.igdb.com/v4/games")
				     .accept(MediaType.APPLICATION_JSON)
				     .header("Client-ID", "j6lkdh0feenmcv3fe3sc33unavvm4j")
				     .header("Authorization", "Bearer 3li86y7jnofe5aetw3wvnopdjxprzp")
				     .body("where id = " + review.getGameID() + "; fields name, cover.image_id, cover.height, cover.width;");
			ResponseEntity<JsonNode> response = template.exchange(request, JsonNode.class);
			DisplayReview dispReview = new DisplayReview (review);
			dispReview.setCoverURL(response.getBody().get(0).get("cover").get("image_id").toString().replace("\"", ""));
			dispReview.setThumbnailURL(response.getBody().get(0).get("cover").get("image_id").toString().replace("\"", ""));
			dispReview.setCoverHeight(Integer.parseInt(response.getBody().get(0).get("cover").get("height").toString()));
			dispReview.setCoverWidth(Integer.parseInt(response.getBody().get(0).get("cover").get("width").toString()));
			return ResponseEntity.status(200).body(dispReview);
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
		System.out.println("made it here to add review");
		try {
			System.out.println(reviewDTO);
			Review review;
			HttpSession session = request.getSession(true);
			reviewDTO.setUser((User)session.getAttribute("currentlyLoggedInUser"));
			DisplayReview dispReview = new DisplayReview(reviewService.postNewReview(reviewDTO));
			return ResponseEntity.status(201).body(dispReview);
		} catch (ReviewAddException e) {
			return ResponseEntity.status(500).body(new MessageDTO("Unable to create review in the database!"));
		} catch (BadParameterException e) {
			return ResponseEntity.status(400).body(new MessageDTO("User provided a bad parameter"));
		}
		
	}
	
	@GetMapping(path = "review/game/{id}")
	public ResponseEntity<Object> getReviewsByGame(@PathVariable("id") String gameID){
		try {
			
			RestTemplate template = new RestTemplate();
			RequestEntity<String> request = RequestEntity
				     .post("https://api.igdb.com/v4/games")
				     .accept(MediaType.APPLICATION_JSON)
				     .header("Client-ID", "j6lkdh0feenmcv3fe3sc33unavvm4j")
				     .header("Authorization", "Bearer 3li86y7jnofe5aetw3wvnopdjxprzp")
				     .body("where id = " + gameID + "; fields name, cover.image_id, cover.height, cover.width;");
			ResponseEntity<JsonNode> response = template.exchange(request, JsonNode.class);
			String imageID = response.getBody().get(0).get("cover").get("image_id").toString().replace("\"", "");
			int coverHeight = Integer.parseInt(response.getBody().get(0).get("cover").get("height").toString());
			int coverWidth = Integer.parseInt(response.getBody().get(0).get("cover").get("width").toString());
			String gameName = response.getBody().get(0).get("name").toString().replace("\"", "");
			ArrayList<Review> reviewList = null;
			reviewList = reviewService.getReviewsByGame(gameID);
			ArrayList<DisplayReview> dispReviewList = new ArrayList<DisplayReview>();
			for(int i=0; i<reviewList.size(); i++) {
				dispReviewList.add(new DisplayReview(reviewList.get(i)));
				dispReviewList.get(i).setCoverHeight(coverHeight);
				dispReviewList.get(i).setCoverWidth(coverWidth);
				dispReviewList.get(i).setThumbnailURL(imageID);
				dispReviewList.get(i).setCoverURL(imageID);
				dispReviewList.get(i).setGameName(gameName);
				//all of these reviews are for the same game so we don't have to worry about checking for mismatches, and we set all of the game
				//specific values to the same game
			}
			return ResponseEntity.status(200).body(dispReviewList);
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
			ArrayList<DisplayReview> dispReviewList = new ArrayList<DisplayReview>();
			String bodyString = "";
			for(int i=0, j=reviewList.size()-1; i<10 && j>=0; i++, j--) {
				if(reviewList.get(j) != null) {
					if(i==0) {
						bodyString += "where id = " + reviewList.get(j).getGameID();
					} else {
						bodyString += " | id = " + reviewList.get(j).getGameID();
					}
					dispReviewList.add(new DisplayReview(reviewList.get(j)));
				} else {
					//null values can indicate a potentially deleted review, so keep going until we get 10
					//decrements i because it's about to increment it
					i--;
				}
			}
			RestTemplate template = new RestTemplate();
			RequestEntity<String> request = RequestEntity
				     .post("https://api.igdb.com/v4/games")
				     .accept(MediaType.APPLICATION_JSON)
				     .header("Client-ID", "j6lkdh0feenmcv3fe3sc33unavvm4j")
				     .header("Authorization", "Bearer 3li86y7jnofe5aetw3wvnopdjxprzp")
				     .body(bodyString + "; fields name, cover.image_id, cover.height, cover.width;");
			ResponseEntity<JsonNode> response = template.exchange(request, JsonNode.class);
			String imageID;
			int checkIndex;
			for(int i=0; i<dispReviewList.size(); i++) {
				checkIndex = i;
				//Duplicate game values will cause issues with the external API, so these checks are to make sure that the correct name and image URLs are
				//attributed to the correct ID without calling the API multiple times (it would be more efficient to call it multiple times, however the API has
				//a limit on the number of times you can call in a short amount of time, so this inefficiency is a way to circumvent that lockout)
				if(response.getBody().get(i) == null || dispReviewList.get(i).getGameID() != Integer.parseInt(response.getBody().get(i).get("id").toString())) {
					for(int j=response.getBody().size()-1; j>=0; j--) {
						if(dispReviewList.get(i).getGameID() == Integer.parseInt(response.getBody().get(j).get("id").toString())) {
							checkIndex = j;
							j = -1;
						}
					}
				}
				if(response.getBody().get(checkIndex).get("cover") != null) {
					imageID = response.getBody().get(checkIndex).get("cover").get("image_id").toString().replace("\"", "");
					dispReviewList.get(i).setThumbnailURL(imageID);
					dispReviewList.get(i).setCoverURL(imageID);
					dispReviewList.get(i).setCoverHeight(Integer.parseInt(response.getBody().get(checkIndex).get("cover").get("height").toString()));
					dispReviewList.get(i).setCoverWidth(Integer.parseInt(response.getBody().get(checkIndex).get("cover").get("width").toString()));
				} else {
					dispReviewList.get(i).setNoArt();
				}
				dispReviewList.get(i).setGameName(response.getBody().get(checkIndex).get("name").toString().replace("\"", ""));
			}
			return ResponseEntity.status(200).body(dispReviewList);
		} catch (ReviewNotFoundException e) {
			return ResponseEntity.status(404).body(new MessageDTO("No Reviews could be found"));
		} catch (EmptyParameterException e) {
			return ResponseEntity.status(400).body(new MessageDTO("Username was left blank"));
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(404).body(new MessageDTO("User was not found"));
		}
	}
	
	
	
	
	@GetMapping(path = "review/all")
	public ResponseEntity<Object> getAllReviews(){
		try {
			ArrayList<Review> reviewList = null;
			reviewList = (ArrayList<Review>) reviewService.getAllReviews();
			ArrayList<DisplayReview> dispReviewList = new ArrayList<DisplayReview>();
			//ArrayList<Integer> gameIDList = new ArrayList<Integer>();
			String bodyString = "";
			for(int i=0, j=reviewList.size()-1; i<10 && j>=0; i++, j--) {
				
				if(reviewList.get(j) != null) {
					if(i==0) {
						bodyString += "where id = " + reviewList.get(j).getGameID();
					} else {
						bodyString += " | id = " + reviewList.get(j).getGameID();
					}
					dispReviewList.add(new DisplayReview(reviewList.get(j)));
				} else {
					//null values can indicate a potentially deleted review, so keep going until we get 10
					//decrements i because it's about to increment it
					i--;
				}
			}
			RestTemplate template = new RestTemplate();
			RequestEntity<String> request = RequestEntity
				     .post("https://api.igdb.com/v4/games")
				     .accept(MediaType.APPLICATION_JSON)
				     .header("Client-ID", "j6lkdh0feenmcv3fe3sc33unavvm4j")
				     .header("Authorization", "Bearer 3li86y7jnofe5aetw3wvnopdjxprzp")
				     .body(bodyString + "; fields name, cover.image_id, cover.height, cover.width;");
			ResponseEntity<JsonNode> response = template.exchange(request, JsonNode.class);
			String imageID;
			int checkIndex;
			for(int i=0; i<dispReviewList.size(); i++) {
				checkIndex = i;
				if(response.getBody().get(i) == null || dispReviewList.get(i).getGameID() != Integer.parseInt(response.getBody().get(i).get("id").toString())) {
					for(int j=response.getBody().size()-1; j>=0; j--) {
						if(dispReviewList.get(i).getGameID() == Integer.parseInt(response.getBody().get(j).get("id").toString())) {
							checkIndex = j;
							j = -1;
						}
					}
				}
				if(response.getBody().get(checkIndex).get("cover") != null) {
					imageID = response.getBody().get(checkIndex).get("cover").get("image_id").toString().replace("\"", "");
					dispReviewList.get(i).setThumbnailURL(imageID);
					dispReviewList.get(i).setCoverURL(imageID);
					dispReviewList.get(i).setCoverHeight(Integer.parseInt(response.getBody().get(checkIndex).get("cover").get("height").toString()));
					dispReviewList.get(i).setCoverWidth(Integer.parseInt(response.getBody().get(checkIndex).get("cover").get("width").toString()));
				} else {
					dispReviewList.get(i).setNoArt();
				}
				dispReviewList.get(i).setGameName(response.getBody().get(checkIndex).get("name").toString().replace("\"", ""));
			}
			return ResponseEntity.status(200).body(dispReviewList);
		}catch (ReviewNotFoundException e) {
			return ResponseEntity.status(404).body(new MessageDTO("No Reviews could be found"));
		}
	}
	
	
	
	@GetMapping(path = "review/recent")
	public ResponseEntity<Object> getTenMostRecentReviews() {
		try {
			ArrayList<Review> reviewList = null;
			reviewList = (ArrayList<Review>) reviewService.getAllReviews();
			ArrayList<DisplayReview> dispReviewList = new ArrayList<DisplayReview>();
			String bodyString = "";
			for(int i=0, j=reviewList.size()-1; i<10 && j>=0; i++, j--) {
				if(reviewList.get(j) != null) {
					if(i==0) {
						bodyString += "where id = " + reviewList.get(j).getGameID();
					} else {
						bodyString += " | id = " + reviewList.get(j).getGameID();
					}
					dispReviewList.add(new DisplayReview(reviewList.get(j)));
				} else {
					//null values can indicate a potentially deleted review, so keep going until we get 10
					//decrements i because it's about to increment it
					i--;
				}
			}
			RestTemplate template = new RestTemplate();
			RequestEntity<String> request = RequestEntity
				     .post("https://api.igdb.com/v4/games")
				     .accept(MediaType.APPLICATION_JSON)
				     .header("Client-ID", "j6lkdh0feenmcv3fe3sc33unavvm4j")
				     .header("Authorization", "Bearer 3li86y7jnofe5aetw3wvnopdjxprzp")
				     .body(bodyString + "; fields name, cover.image_id, cover.height, cover.width;");
			ResponseEntity<JsonNode> response = template.exchange(request, JsonNode.class);
			String imageID;
			int checkIndex;
			for(int i=0; i<10; i++) {
				//in case there are less than 10 reviews in the database
				if(i<dispReviewList.size()) {
					checkIndex = i;
					if(response.getBody().get(i) == null || dispReviewList.get(i).getGameID() != Integer.parseInt(response.getBody().get(i).get("id").toString())) {
						for(int j=response.getBody().size()-1; j>=0; j--) {
							if(dispReviewList.get(i).getGameID() == Integer.parseInt(response.getBody().get(j).get("id").toString())) {
								checkIndex = j;
								j = -1;
							}
						}
					}
					if(response.getBody().get(checkIndex).get("cover") != null) {
						imageID = response.getBody().get(checkIndex).get("cover").get("image_id").toString().replace("\"", "");
						dispReviewList.get(i).setThumbnailURL(imageID);
						dispReviewList.get(i).setCoverURL(imageID);
						dispReviewList.get(i).setCoverHeight(Integer.parseInt(response.getBody().get(checkIndex).get("cover").get("height").toString()));
						dispReviewList.get(i).setCoverWidth(Integer.parseInt(response.getBody().get(checkIndex).get("cover").get("width").toString()));
					} else {
						dispReviewList.get(i).setNoArt();
					}
					dispReviewList.get(i).setGameName(response.getBody().get(checkIndex).get("name").toString().replace("\"", ""));
				} else {
					//If it's null, that means we couldn't fill up with 10 reviews for some reason so just cap it here
					i = 10;
				}
			}
			return ResponseEntity.status(200).body(dispReviewList);
		}catch (ReviewNotFoundException e) {
			return ResponseEntity.status(404).body(new MessageDTO("No Reviews could be found"));
		}
	}
}
