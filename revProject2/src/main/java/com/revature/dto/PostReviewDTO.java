package com.revature.dto;

import com.revature.model.ReviewStatus;
import com.revature.model.User;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString @EqualsAndHashCode
public class PostReviewDTO {
	
	private User user;
	private int rating;
	private String description;
	private double hoursPlayed;
	private ReviewStatus completionStatus;
	private int gameID;
}
