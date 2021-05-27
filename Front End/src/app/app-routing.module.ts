import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SignupComponent } from './signup/signup.component';
import { LoginComponent } from './login/login.component';
import { PostGameReviewComponent } from './post-game-review/post-game-review.component';
import { ReviewsComponent } from './reviews/reviews.component';
import { HomeComponent } from './home/home.component';
import { SearchGameComponent } from './search-game/search-game.component';
import { ReadReviewsComponent } from './read-reviews/read-reviews.component';
import { UserProfileComponent } from './user-profile/user-profile.component';


const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'postGameReview', component: PostGameReviewComponent },
  { path: 'reviews', component: ReviewsComponent },
  { path: 'user-profile', component: UserProfileComponent},
  { path: 'home', component: HomeComponent },
  { path: 'searchGame', component: SearchGameComponent },
  { path: 'readGameReview', component: ReadReviewsComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
