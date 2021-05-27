import { Component, OnInit } from '@angular/core';
import { Game } from '../../model/Game';
import { Review } from '../../model/review';
import { ReviewService } from '../review.service';
import { GameService } from '../game.service';


@Component({
  selector: 'app-read-reviews',
  templateUrl: './read-reviews.component.html',
  styleUrls: ['./read-reviews.component.css']
})
export class ReadReviewsComponent implements OnInit {
  reviewService: ReviewService;
  gameService: GameService;
  reviews: Review[] = [];
  gameName: string;
  bigCoverURL: string;
  coverHeight: number;
  coverWidth: number;
  averageScore: string;

  constructor(gameService: GameService,reviewService: ReviewService) { 
    this.reviewService = reviewService;
    this.gameService = gameService;
  }

  ngOnInit(): void {
    this.reviewService.getReviewsByGameID(this.gameService.getGameId()).subscribe((reviews) => {
      console.log(reviews);
      this.reviews = reviews;
      this.gameName = this.gameService.getGameName();
      this.bigCoverURL = this.gameService.getCoverURL();
      console.log("bigCover: " + this.bigCoverURL);
      this.coverHeight = this.gameService.getCoverHeight();
      this.coverWidth = this.gameService.getCoverWidth();
      if(reviews.length != 0){
        this.averageScore = getAverageScore(reviews) + "/10";
      } else {
        this.averageScore = "No Scores Submitted"
      }
      
    });
  }

  

}

function getAverageScore(reviews: Review[]){
    let averageScore = 0;
    for(let i=0; i<reviews.length; i++){
      averageScore += reviews[i].rating;
    }
    return averageScore /= reviews.length;
}
