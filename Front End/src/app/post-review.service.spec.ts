import { TestBed } from '@angular/core/testing';

import { PostReviewService } from './post-review.service';

describe('PostReviewService', () => {
  let service: PostReviewService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PostReviewService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
