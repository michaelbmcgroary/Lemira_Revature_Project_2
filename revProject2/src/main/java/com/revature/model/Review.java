package com.revature.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.revature.dto.PostReviewDTO;

@Entity
@Table(name="glb_reviews")
public class Review {


		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "reviewID")
		private int reviewID;
		
		@ManyToOne
		@JoinColumn(name = "userID")
		private User user;
		
		@Column(name = "review_rating")
		private int rating;
		
		@Column(name = "review_description", length = 500)
		private String description;
		
		@Column(name = "review_hours_played")
		private double hoursPlayed;
		
		@ManyToOne
		@JoinColumn(name = "review_completion_status_id")
		private ReviewStatus completionStatus;
		
		//Change this to a join column after getting the games from an API if needed
		@Column(name = "game_id")
		private int gameID;

		public Review() {
			super();
		}

		public Review(User user, int rating, String description, double hoursPlayed, ReviewStatus completionStatus, int gameID) {
			super();
			this.user = user;
			this.rating = rating;
			this.description = description;
			this.hoursPlayed = hoursPlayed;
			this.completionStatus = completionStatus;
			this.gameID = gameID;
		}
		
		public Review(PostReviewDTO reviewDTO) {
			this.user = reviewDTO.getUser();
			this.rating = reviewDTO.getRating();
			this.description = reviewDTO.getDescription();
			this.hoursPlayed = reviewDTO.getHoursPlayed();
			this.completionStatus = reviewDTO.getCompletionStatus();
			this.gameID = reviewDTO.getGameID();
		}

		public int getReviewID() {
			return reviewID;
		}

		public void setReviewID(int reviewID) {
			this.reviewID = reviewID;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public int getRating() {
			return rating;
		}

		public void setRating(int rating) {
			this.rating = rating;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public double getHoursPlayed() {
			return hoursPlayed;
		}

		public void setHoursPlayed(double hoursPlayed) {
			this.hoursPlayed = hoursPlayed;
		}

		public ReviewStatus getCompletionStatus() {
			return completionStatus;
		}

		public void setCompletionStatus(ReviewStatus completionStatus) {
			this.completionStatus = completionStatus;
		}

		public int getGameID() {
			return gameID;
		}

		public void setGameID(int gameID) {
			this.gameID = gameID;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((completionStatus == null) ? 0 : completionStatus.hashCode());
			result = prime * result + ((description == null) ? 0 : description.hashCode());
			result = prime * result + gameID;
			long temp;
			temp = Double.doubleToLongBits(hoursPlayed);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result + rating;
			result = prime * result + reviewID;
			result = prime * result + ((user == null) ? 0 : user.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Review other = (Review) obj;
			if (completionStatus == null) {
				if (other.completionStatus != null)
					return false;
			} else if (!completionStatus.equals(other.completionStatus))
				return false;
			if (description == null) {
				if (other.description != null)
					return false;
			} else if (!description.equals(other.description))
				return false;
			if (gameID != other.gameID)
				return false;
			if (Double.doubleToLongBits(hoursPlayed) != Double.doubleToLongBits(other.hoursPlayed))
				return false;
			if (rating != other.rating)
				return false;
			if (reviewID != other.reviewID)
				return false;
			if (user == null) {
				if (other.user != null)
					return false;
			} else if (!user.equals(other.user))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Review [reviewID=" + reviewID + ", user=" + user + ", rating=" + rating + ", description="
					+ description + ", hoursPlayed=" + hoursPlayed + ", completionStatus=" + completionStatus
					+ ", gameID=" + gameID + "]";
		}
		
		
}