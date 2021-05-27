import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Review} from '../../model/review'


@Component({
  selector: 'app-review-item',
  templateUrl: './review-item.component.html',
  styleUrls: ['./review-item.component.css']
})
export class ReviewItemComponent implements OnInit {
  @Input() review: Review;
  @Output() onDeleteReview: EventEmitter<Review> = new EventEmitter();

  constructor() { }

  ngOnInit(): void {
  }

  onDelete(review: Review) {
    console.log("clicking onDelete");
    this.onDeleteReview.emit(review);
  }

}
