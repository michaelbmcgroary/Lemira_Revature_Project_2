import { Component, OnInit } from '@angular/core';
import { Review } from '../../model/review'
import { ReviewService } from '../review.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  reviews: Review[] = [];

  constructor(private reviewService: ReviewService) { }

  ngOnInit(): void {
    this.reviewService.getRecentReviews().subscribe((reviews) => (console.log(reviews),
      this.reviews = reviews
    ));
  }

}
