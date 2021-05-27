import { Component } from '@angular/core';
import { User } from 'src/model/User';
import { LoginService } from './login.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'project2';
  loginService: LoginService;
  user: User;
  password: string;
  username: string;
  isUserLoggedIn: boolean = false;
  isUserModerator: boolean = false;

  constructor(loginService: LoginService, private router: Router) {
    this.loginService = loginService;
  }

  ngOnInit(): void {
    this.router.navigateByUrl(`/home`);
  }


  ngDoCheck(): void {
    this.isUserLoggedIn = this.loginService.getUserStatus();
    //console.log("inside ngOnChanges() " + this.isUserLoggedIn)
    this.isUserModerator = this.loginService.getUserRole();
  }

  

  logoutUser() {
    this.loginService.logoutUser(this.user).subscribe((response) => {
      this.router.navigateByUrl(`/login`)
      //console.log("user logout" + response);
      this.loginService.setUserStatus();
    })

  }


}
