import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/model/User';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private isUserLoggedIn: boolean = false;
  private isUserModerator: boolean = true;
  //Update this value so that not everyone is a moderator
  constructor(private httpClient: HttpClient) { }

  getUserLoginAndPassword(usernameInput: string, passwordInput: string): Observable<User> {
    console.log("inside userLoginAndPassword() " + usernameInput + passwordInput);
    return this.httpClient.post<User>(`http://ec2-52-14-217-72.us-east-2.compute.amazonaws.com:8080/revProject2/login`, { username: usernameInput, password: passwordInput }, { withCredentials: true });

  }

  confirmLogin(): Observable<User> {
    return this.httpClient.get<User>(`http://ec2-52-14-217-72.us-east-2.compute.amazonaws.com:8080/revProject2/user/currentUser`, {
      withCredentials: true,
    });
  }

  logoutUser(user: User): Observable<Object> {
    return this.httpClient.post<Object>(`http://ec2-52-14-217-72.us-east-2.compute.amazonaws.com:8080/revProject2/logout`, Object, { withCredentials: true });
  }

  setUserStatus() {
    this.isUserLoggedIn = !this.isUserLoggedIn;
  }

  getUserStatus() {
    return this.isUserLoggedIn;
  }

  getUserRole() {
    return this.isUserModerator;
  }

}
