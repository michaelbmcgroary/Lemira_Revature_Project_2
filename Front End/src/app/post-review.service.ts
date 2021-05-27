import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PostReviewDTO } from 'src/model/postReviewDTO';

@Injectable({
  providedIn: 'root'
})
export class PostReviewService {


  constructor(private httpClient: HttpClient) { }


  postReview(postReviewDTO: PostReviewDTO) {
    console.log("inside postReview() " + postReviewDTO);
    return this.httpClient.post<PostReviewDTO>(`http://ec2-52-14-217-72.us-east-2.compute.amazonaws.com:8080/revProject2/review`, postReviewDTO, { withCredentials: true });

  }

}
