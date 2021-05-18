package com.revature.app;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.controller.ExternalRequestController;
import com.revature.exception.PasswordHashException;
import com.revature.model.User;
import com.revature.model.UserStatus;
import com.revature.model.UserType;



public class Application {

	
	public static void main(String[] args) throws PasswordHashException {
		
		//testToObject();
		
		//User user = new User(1, "Username", "YoThisIsMyPassword", "George", "Lucas", "glucas@gmail.com", new UserType(1), new UserStatus(1));
		//System.out.println(user.getPassword());
		
	}
	
	
	public static void getPokemon(){
		RestTemplate template = new RestTemplate();
		RequestEntity<String> request = RequestEntity
			     .post("https://api.igdb.com/v4/games")
			     .accept(MediaType.APPLICATION_JSON)
			     .header("Client-ID", "j6lkdh0feenmcv3fe3sc33unavvm4j")
			     .header("Authorization", "Bearer 3li86y7jnofe5aetw3wvnopdjxprzp")
			     .body("search \"danganronpa\"; fields id, name;");
		System.out.println(request.toString());
		ResponseEntity<String> response = template.exchange(request, String.class);
		System.out.println(response.toString());
	}
	
	public static void testToObject(){
		int gameID = 121752;
		RestTemplate template = new RestTemplate();
		RequestEntity<String> request = RequestEntity
			     .post("https://api.igdb.com/v4/covers")
			     .accept(MediaType.APPLICATION_JSON)
			     .header("Client-ID", "j6lkdh0feenmcv3fe3sc33unavvm4j")
			     .header("Authorization", "Bearer 3li86y7jnofe5aetw3wvnopdjxprzp")
			     .body("where game = " + gameID + " | game = 532; fields image_id, width, height;");
		ResponseEntity<JsonNode> response = template.exchange(request, JsonNode.class);
		System.out.println(response.getBody());
		String imageID = response.getBody().get(0).get("image_id").toString().replace("\"", "");
		int coverWidth = Integer.parseInt(response.getBody().get(0).get("width").toString());
		int coverHeight = Integer.parseInt(response.getBody().get(0).get("height").toString());
		System.out.println(imageID);
		
	}

}
