import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PostReviewDTO } from '../../model/PostReviewDTO';
import { GameService } from '../game.service';
import { PostReviewService } from '../post-review.service';

@Component({
  selector: 'app-post-game-review',
  templateUrl: './post-game-review.component.html',
  styleUrls: ['./post-game-review.component.css']
})
export class PostGameReviewComponent implements OnInit {
  gameNameInput: string;
  dummyGameNameInput = 523;

  ratingInput: string; // parse int
  descriptionInput: string;
  hoursPlayedInput: number; // parse int
  completionInput: number; // int

  postReviewDTO: PostReviewDTO;

  gameName: string = "";

  postReviewService: PostReviewService;
  gameService: GameService;

  constructor(postReviewService: PostReviewService, gameService: GameService, private router: Router) {
    this.postReviewService = postReviewService
    this.gameService = gameService;
  }

  ngOnInit(): void {
  }

  ngDoCheck(): void {
    this.gameName = this.gameService.getGameName();
  }

  submitReview() {
    console.log(this.gameNameInput + " " + this.ratingInput + " " + this.descriptionInput + " " + this.hoursPlayedInput + " " + this.completionInput);
    this.postReviewDTO = {
      rating: Number(this.ratingInput),
      description: this.descriptionInput,
      hoursPlayed: Number(this.hoursPlayedInput),
      completionStatus: Number(this.completionInput),
      gameID: this.gameService.getGameId()
    }

    console.log(this.postReviewDTO);
    this.postReviewService.postReview(this.postReviewDTO).subscribe((response) => {
      //if it even gets here, it means the post review was successful
      this.router.navigateByUrl(`/user-profile`);
    });
  }

}

