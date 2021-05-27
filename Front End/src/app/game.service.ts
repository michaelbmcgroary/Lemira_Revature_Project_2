import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GameService {
  private gameName: string;
  private gameId: number;
  private coverURL: string;
  private coverWidth: number;
  private coverHeight: number;


  setGameName(gameName: string) {
    this.gameName = gameName;
  }
  getGameName() {
    return this.gameName;
  }

  setGameId(gameId: number) {
    this.gameId = gameId;
  }

  setCoverURL(coverURL: string){
    this.coverURL = coverURL;
  }

  setCoverHeight(coverHeight: number){
    this.coverHeight = coverHeight;
  }

  setCoverWidth(coverWidth: number){
    this.coverWidth = coverWidth;
  }

  getCoverHeight(){
    return this.coverHeight;
  }

  getCoverWidth(){
    return this.coverWidth;
  }

  getCoverURL(){
    return this.coverURL;
  }

  getGameId() {
    return this.gameId;
  }
  constructor() { }
}
