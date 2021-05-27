import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostGameReviewComponent } from './post-game-review.component';

describe('PostGameReviewComponent', () => {
  let component: PostGameReviewComponent;
  let fixture: ComponentFixture<PostGameReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PostGameReviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PostGameReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
