export interface Review {
  reviewId?: number;
  rating: number;
  description: string;
  hoursPlayed: number;
  completionStatus: number;
  gameName: string;
  username: string;
  imageUrl: string;
}
