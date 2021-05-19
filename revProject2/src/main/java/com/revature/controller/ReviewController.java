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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.revature.annotations.LoggedInOnly;
import com.revature.annotations.ModeratorOnly;
import com.revature.annotations.UnbannedOnly;
import com.revature.dto.DisplayReview;
import com.revature.dto.MessageDTO;
import com.revature.dto.PostReviewDTO;
import com.revature.exception.BadParameterException;
import com.revature.exception.EmptyParameterException;
import com.revature.exception.ExternalAPIConnectException;
import com.revature.exception.ReviewAddException;
import com.revature.exception.ReviewNotFoundException;
import com.revature.exception.UserNotFoundException;
import com.revature.model.Review;
import com.revature.model.User;
import com.revature.service.ReviewService;


//@CrossOrigin(allowCredentials = "true", origins = "*", allowedHeaders = "*")
@CrossOrigin( allowCredentials = "true" ,origins = "http://localhost:4200")
//@CrossOrigin(origins = "*", allowCredentials = "true")
//@CrossOrigin(origins = "*")
@Controller // This is a stereotype annotation, just like @Component, @Service, @Repository
// What those annotations are for, is to have Spring register it as a Spring Bean
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private HttpServletRequest request;
	
	@SuppressWarnings("unused")
	@Autowired
	private HttpServletResponse response;

	
	
	@GetMapping(path = "review/{id}")
	@LoggedInOnly
	public ResponseEntity<Object> getReviewById(@PathVariable("id") String id) {
		DisplayReview dispReview = null; 
		try {
			Review review = reviewService.getReviewByID(id);
			dispReview = new DisplayReview (review);
			RestTemplate template = new RestTemplate();
			RequestEntity<String> request = RequestEntity
				     .post("https://api.igdb.com/v4/games")
				     .accept(MediaType.APPLICATION_JSON)
				     .header("Client-ID", "j6lkdh0feenmcv3fe3sc33unavvm4j")
				     .header("Authorization", "Bearer 3li86y7jnofe5aetw3wvnopdjxprzp")
				     .header("User-Agent", "My own REST client")
				     .body("where id = " + review.getGameID() + "; fields name, cover.image_id, cover.height, cover.width;");
			ResponseEntity<JsonNode> response;
			try {
				response = template.exchange(request, JsonNode.class);
			} catch (HttpClientErrorException e) {
				throw new ExternalAPIConnectException();
			}
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
		} catch (ExternalAPIConnectException e) {
			dispReview.setNoConnection();
			return ResponseEntity.status(403).body(dispReview);
		}
	}
	
	
	@PostMapping(path = "review")
	@LoggedInOnly
	@UnbannedOnly
	public ResponseEntity<Object> addReview(@RequestBody PostReviewDTO reviewDTO) {
		try {
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
				     .header("User-Agent", "My own REST client")
				     .body("where id = " + gameID + "; fields name, cover.image_id, cover.height, cover.width;");
			ResponseEntity<JsonNode> response;
			try {
				response = template.exchange(request, JsonNode.class);
			} catch (HttpClientErrorException e) {
				throw new ExternalAPIConnectException();
			}
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
		} catch (ExternalAPIConnectException e) {
			return ResponseEntity.status(403).body(new MessageDTO("IGDB connectivity is unstable, please try again later"));
		}
	}
	
	
	
	@GetMapping(path = "user/{username}")
	public ResponseEntity<Object> getReviewsByUser(@PathVariable("username") String username){
		ArrayList<Review> reviewList = null;
		ArrayList<DisplayReview> dispReviewList = new ArrayList<DisplayReview>();
		try {
			reviewList = (ArrayList<Review>) reviewService.getReviewsByUser(username);
			ArrayList<String> bodyStrings = new ArrayList<String>();
			int numStrings = -1;
			//starting at negative 1 since it will increase immediately
			//groups the strings to query the external api by 10
			for(int i=0; i<reviewList.size(); i++) {
				if(i%10 == 0) {
					numStrings++;
					bodyStrings.add("");
				}
				if(reviewList.get(i) != null) {
					if(i%10 == 0) {
						bodyStrings.set(numStrings, bodyStrings.get(numStrings) + "where id = " + reviewList.get(i).getGameID());
					} else {
						bodyStrings.set(numStrings, bodyStrings.get(numStrings) + "| id = " + reviewList.get(i).getGameID());
					}
					dispReviewList.add(new DisplayReview(reviewList.get(i)));
				} else {
					//null values can indicate a potentially deleted review, so keep going until we get 10
					//decrements i because it's about to increment it
					i--;
				}
			}
			ArrayList<ResponseEntity<JsonNode>> responseList = new ArrayList<ResponseEntity<JsonNode>>();
			for(int m=0; m<=numStrings; m++) {
				RestTemplate template = new RestTemplate();
				RequestEntity<String> request = RequestEntity
					     .post("https://api.igdb.com/v4/games")
					     .accept(MediaType.APPLICATION_JSON)
					     .header("Client-ID", "j6lkdh0feenmcv3fe3sc33unavvm4j")
					     .header("Authorization", "Bearer 3li86y7jnofe5aetw3wvnopdjxprzp")
					     .header("User-Agent", "My own REST client")
					     .body(bodyStrings.get(m) + "; fields name, cover.image_id, cover.height, cover.width;");
				ResponseEntity<JsonNode> response;
				try {
					response = template.exchange(request, JsonNode.class);
				} catch (HttpClientErrorException e) {
					throw new ExternalAPIConnectException();
				}
				responseList.add(response);
				//The external API has a 10 ID limit when querying, so the responses have to be grouped by 10 and checked for duplicate games among those 10 entries
				String imageID;
				int checkIndex;
				for(int i=(m*10), k=0; i<dispReviewList.size() && k<10; i++, k++) {
					checkIndex = i%10;
					if(responseList.get(m).getBody().get(checkIndex) == null || dispReviewList.get(i).getGameID() != Integer.parseInt(responseList.get(m).getBody().get(checkIndex).get("id").toString())) {
						for(int j=responseList.get(m).getBody().size()-1; j>=0; j--) {
							if(dispReviewList.get(i).getGameID() == Integer.parseInt(responseList.get(m).getBody().get(j).get("id").toString())) {
								checkIndex = j;
								j = -1;
							}
						}
					}
					if(responseList.get(m).getBody().get(checkIndex).get("cover") != null) {
						imageID = responseList.get(m).getBody().get(checkIndex).get("cover").get("image_id").toString().replace("\"", "");
						dispReviewList.get(i).setThumbnailURL(imageID);
						dispReviewList.get(i).setCoverURL(imageID);
						dispReviewList.get(i).setCoverHeight(Integer.parseInt(responseList.get(m).getBody().get(checkIndex).get("cover").get("height").toString()));
						dispReviewList.get(i).setCoverWidth(Integer.parseInt(responseList.get(m).getBody().get(checkIndex).get("cover").get("width").toString()));
					} else {
						dispReviewList.get(i).setNoArt();
					}
					dispReviewList.get(i).setGameName(responseList.get(m).getBody().get(checkIndex).get("name").toString().replace("\"", ""));
				}
			}
			return ResponseEntity.status(200).body(dispReviewList);
		} catch (ReviewNotFoundException e) {
			return ResponseEntity.status(404).body(new MessageDTO("No Reviews could be found"));
		} catch (EmptyParameterException e) {
			return ResponseEntity.status(400).body(new MessageDTO("Username was left blank"));
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(404).body(new MessageDTO("User was not found"));
		} catch (ExternalAPIConnectException e) {
			for(int i=0; i<dispReviewList.size(); i++) {
				dispReviewList.get(i).setNoConnection();
			}
			return ResponseEntity.status(403).body(dispReviewList);
		}
	}
	
	
	
	
	@GetMapping(path = "review/all")
	public ResponseEntity<Object> getAllReviews(){
		ArrayList<DisplayReview> dispReviewList = new ArrayList<DisplayReview>();
		try {
			ArrayList<Review> reviewList = null;
			reviewList = (ArrayList<Review>) reviewService.getAllReviews();
			ArrayList<String> bodyStrings = new ArrayList<String>();
			int numStrings = -1;
			//starting at negative 1 since it will increase immediately
			//groups the strings to query the external api by 10
			for(int i=0; i<reviewList.size(); i++) {
				if(i%10 == 0) {
					numStrings++;
					bodyStrings.add("");
				}
				if(reviewList.get(i) != null) {
					if(i%10 == 0) {
						bodyStrings.set(numStrings, bodyStrings.get(numStrings) + "where id = " + reviewList.get(i).getGameID());
					} else {
						bodyStrings.set(numStrings, bodyStrings.get(numStrings) + "| id = " + reviewList.get(i).getGameID());
					}
					dispReviewList.add(new DisplayReview(reviewList.get(i)));
				} else {
					//null values can indicate a potentially deleted review, so keep going until we get 10
					//decrements i because it's about to increment it
					i--;
				}
			}
			ArrayList<ResponseEntity<JsonNode>> responseList = new ArrayList<ResponseEntity<JsonNode>>();
			for(int m=0; m<=numStrings; m++) {
				RestTemplate template = new RestTemplate();
				RequestEntity<String> request = RequestEntity
					     .post("https://api.igdb.com/v4/games")
					     .accept(MediaType.APPLICATION_JSON)
					     .header("Client-ID", "j6lkdh0feenmcv3fe3sc33unavvm4j")
					     .header("Authorization", "Bearer 3li86y7jnofe5aetw3wvnopdjxprzp")
					     .header("User-Agent", "My own REST client")
					     .body(bodyStrings.get(m) + "; fields name, cover.image_id, cover.height, cover.width;");
				ResponseEntity<JsonNode> response;
				try {
					response = template.exchange(request, JsonNode.class);
				} catch (HttpClientErrorException e) {
					throw new ExternalAPIConnectException();
				}
				responseList.add(response);
				//The external API has a 10 ID limit when querying, so the responses have to be grouped by 10 and checked for duplicate games among those 10 entries
				String imageID;
				int checkIndex;
				for(int i=(m*10), k=0; i<dispReviewList.size() && k<10; i++, k++) {
					checkIndex = i%10;
					if(responseList.get(m).getBody().get(checkIndex) == null || dispReviewList.get(i).getGameID() != Integer.parseInt(responseList.get(m).getBody().get(checkIndex).get("id").toString())) {
						for(int j=responseList.get(m).getBody().size()-1; j>=0; j--) {
							if(dispReviewList.get(i).getGameID() == Integer.parseInt(responseList.get(m).getBody().get(j).get("id").toString())) {
								checkIndex = j;
								j = -1;
							}
						}
					}
					if(responseList.get(m).getBody().get(checkIndex).get("cover") != null) {
						imageID = responseList.get(m).getBody().get(checkIndex).get("cover").get("image_id").toString().replace("\"", "");
						dispReviewList.get(i).setThumbnailURL(imageID);
						dispReviewList.get(i).setCoverURL(imageID);
						dispReviewList.get(i).setCoverHeight(Integer.parseInt(responseList.get(m).getBody().get(checkIndex).get("cover").get("height").toString()));
						dispReviewList.get(i).setCoverWidth(Integer.parseInt(responseList.get(m).getBody().get(checkIndex).get("cover").get("width").toString()));
					} else {
						dispReviewList.get(i).setNoArt();
					}
					dispReviewList.get(i).setGameName(responseList.get(m).getBody().get(checkIndex).get("name").toString().replace("\"", ""));
				}
			}
			return ResponseEntity.status(200).body(dispReviewList);
		}catch (ReviewNotFoundException e) {
			return ResponseEntity.status(404).body(new MessageDTO("No Reviews could be found"));
		} catch (ExternalAPIConnectException e) {
			for(int i=0; i<dispReviewList.size(); i++) {
				dispReviewList.get(i).setNoConnection();
			}
			return ResponseEntity.status(403).body(dispReviewList);
		}
	}
	
	
	
	@GetMapping(path = "review/recent")
	public ResponseEntity<Object> getTenMostRecentReviews() {
		ArrayList<DisplayReview> dispReviewList = new ArrayList<DisplayReview>();
		try {
			ArrayList<Review> reviewList = null;
			reviewList = (ArrayList<Review>) reviewService.getAllReviews();
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
				     .header("User-Agent", "My own REST client")
				     .body(bodyString + "; fields name, cover.image_id, cover.height, cover.width;");
			ResponseEntity<JsonNode> response;
			try {
				response = template.exchange(request, JsonNode.class);
			} catch (HttpClientErrorException e) {
				throw new ExternalAPIConnectException();
			}
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
					//If it's null, that means we couldn't fill up with 10 reviews for some reason (most likely reason is that while in testing, we're handing less than 10) so just cap it here
					i = 10;
				}
			}
			return ResponseEntity.status(200).body(dispReviewList);
		} catch (ReviewNotFoundException e) {
			return ResponseEntity.status(404).body(new MessageDTO("No Reviews could be found"));
		} catch (ExternalAPIConnectException e) {
			for(int i=0; i<dispReviewList.size(); i++) {
				dispReviewList.get(i).setNoConnection();
			}
			return ResponseEntity.status(403).body(dispReviewList);
		}
	}
}
