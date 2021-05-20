package com.revature.controller;

import java.net.URI;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.revature.dto.MessageDTO;
import com.revature.exception.ExternalAPIConnectException;
import com.revature.model.Game;

@CrossOrigin( allowCredentials = "true" ,origins = "http://localhost:8080")
@Controller
public class ExternalRequestController {
	

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private HttpServletResponse response;

	
	@GetMapping(path = "game/search/{name}")
	public ResponseEntity<Object> searchForGames(@PathVariable("name") String name) {
		ArrayList<Game> gameSearchList = new ArrayList<Game>();
		try {
			RestTemplate template = new RestTemplate();
			RequestEntity<String> request = RequestEntity
				     .post("https://api.igdb.com/v4/games")
				     .accept(MediaType.TEXT_PLAIN)
				     .header("Client-ID", "j6lkdh0feenmcv3fe3sc33unavvm4j")
				     .header("Authorization", "Bearer 3li86y7jnofe5aetw3wvnopdjxprzp")
				     .header("User-Agent", "My own REST client")
				     .body("search \"" + name + "\"; fields id, name, cover.image_id, cover.height, cover.width;");
			ResponseEntity<JsonNode> response;
			try {
				response = template.exchange(request, JsonNode.class);
			} catch (HttpClientErrorException e) {
				throw new ExternalAPIConnectException();
			}
			String imageID;
			for(int i=0; i<response.getBody().size(); i++) {
				gameSearchList.add(new Game());
				gameSearchList.get(i).setGameID(Integer.parseInt(response.getBody().get(i).get("id").toString()));
				gameSearchList.get(i).setGameName(response.getBody().get(i).get("name").toString().replace("\"", ""));
				if(response.getBody().get(i).get("cover") != null) {
					imageID = response.getBody().get(i).get("cover").get("image_id").toString().replace("\"", "");
					gameSearchList.get(i).setThumbnailURL(imageID);
					gameSearchList.get(i).setCoverURL(imageID);
					gameSearchList.get(i).setCoverHeight(Integer.parseInt(response.getBody().get(i).get("cover").get("height").toString()));
					gameSearchList.get(i).setCoverWidth(Integer.parseInt(response.getBody().get(i).get("cover").get("width").toString()));
				} else {
					gameSearchList.get(i).setNoArt();
				}
			}
			
			if(gameSearchList.size() > 0) {
				return ResponseEntity.status(200).body(gameSearchList);
			} else {
				return ResponseEntity.status(404).body(new MessageDTO("No results for search"));
			}
		} catch (ExternalAPIConnectException e) {
			gameSearchList.add(new Game());
			gameSearchList.get(0).setNoConnection();
			return ResponseEntity.status(403).body(gameSearchList);
		}
		
		
	}
	
	
}
