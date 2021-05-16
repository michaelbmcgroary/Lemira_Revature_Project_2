package com.revature.dto;

import com.revature.model.Review;
import com.revature.model.ReviewStatus;
import com.revature.model.User;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString @EqualsAndHashCode
public class DisplayReview {
	
	private String username;
	private int rating;
	private String description;
	private double hoursPlayed;
	private String completionStatus;
	private int gameID;
	
	public DisplayReview(Review review) {
		this.username = review.getUser().getUsername();
		this.rating = review.getRating();
		this.description = review.getDescription();
		this.hoursPlayed = review.getHoursPlayed();
		this.completionStatus = review.getCompletionStatus().getStatus();
		this.gameID = review.getGameID();
	}
}
