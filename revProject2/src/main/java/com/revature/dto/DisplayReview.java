package com.revature.dto;

import com.revature.model.Review;

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
	private String gameName;
	private String thumbnailURL;
	private String coverURL;
	private int coverHeight;
	private int coverWidth;
	
	public DisplayReview(Review review) {
		this.username = review.getUser().getUsername();
		this.rating = review.getRating();
		this.description = review.getDescription();
		this.hoursPlayed = review.getHoursPlayed();
		this.completionStatus = review.getCompletionStatus().getStatus();
		this.gameID = review.getGameID();
	}
	
	public void setThumbnailURL(String imageID) {
		this.thumbnailURL = "https://images.igdb.com/igdb/image/upload/t_thumb/" + imageID + ".jpg";
	}
	
	public void setCoverURL(String imageID) {
		this.coverURL = "https://images.igdb.com/igdb/image/upload/t_cover_big/" + imageID + ".jpg";
	}
	
	public void setNoArt() {
		//These are being hosted on discord for now but if this was a full production, they would be hosted on the actual server
		this.thumbnailURL = "https://cdn.discordapp.com/attachments/506514654764466197/844277974701572136/gameNotFoundThumb.jpg";
		this.coverURL = "https://cdn.discordapp.com/attachments/506514654764466197/844277972261011456/gameNotFoundCover.jpg";
		this.coverHeight = 352;
		this.coverWidth = 264;
	}
	
	public void setNoConnection() {
		setNoArt();
		this.gameName = "IGDB Connection Unstable";
		
	}
}
