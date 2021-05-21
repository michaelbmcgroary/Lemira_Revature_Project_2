package com.revature.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString @EqualsAndHashCode
public class Game {
	
	private int gameID;
	private String gameName;
	private String thumbnailURL;
	private String coverURL;
	private int coverHeight;
	private int coverWidth;
	
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
		gameID = 0;
		gameName = "IGDB Connection Unstable";
		setNoArt();
	}
}
