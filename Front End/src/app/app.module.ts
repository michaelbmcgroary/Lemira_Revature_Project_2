import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms'
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { PostGameReviewComponent } from './post-game-review/post-game-review.component';
import { ReviewsComponent } from './reviews/reviews.component';
import { ReviewItemComponent } from './review-item/review-item.component';


import { LoginService } from './login.service';
import { SignupService } from './signup.service';
import { PostReviewService } from './post-review.service';
import { ReviewService } from './review.service';
import { HomeComponent } from './home/home.component';
import { SearchGameComponent } from './search-game/search-game.component';
import { UserProfileComponent } from './user-profile/user-profile.component';
import { ReadReviewsComponent } from './read-reviews/read-reviews.component';
import { GameService } from './game.service';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignupComponent,
    PostGameReviewComponent,
    ReviewsComponent,
    ReviewItemComponent,
    HomeComponent,
    SearchGameComponent,
    UserProfileComponent,
    ReadReviewsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
  ],
  providers: [
    LoginService,
    SignupService,
    PostReviewService,
    ReviewService,
    GameService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
