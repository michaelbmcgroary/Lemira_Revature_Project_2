import { Component, OnInit } from '@angular/core';
import { Review } from '../../model/review';
import { ReviewService } from '../review.service';



@Component({
  selector: 'app-reviews',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  reviews: Review[] = [];
  searchUsernameInput: string;
  currentUsername: string;

  constructor(private reviewService: ReviewService) { }

  //get rid of session storage, move the session storage to the app component for the my reviews button
  ngOnInit(): void {
    this.currentUsername = sessionStorage.getItem('username');
    this.reviewService.getReviewsByUser(sessionStorage.getItem('username')).subscribe((reviews) => (console.log(reviews),
      this.reviews = reviews
    ));
  }

  searchUser() {
    console.log("searchGameInput: " + this.searchUsernameInput);
    this.reviewService.getReviewsByUser(this.searchUsernameInput).subscribe((reviews) => {
      console.log(reviews);
      this.reviews = reviews;
      this.currentUsername = this.searchUsernameInput;
    });
  }


  deleteReview(review: Review) {
    this.reviewService
      .deleteReview(review)
      .subscribe(
        () => (this.reviews = this.reviews.filter((r) => r.reviewId !== review.reviewId))
      );
  }
}
