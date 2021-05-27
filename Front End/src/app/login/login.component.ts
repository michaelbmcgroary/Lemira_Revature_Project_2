import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

import { LoginService } from '../login.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  usernameInput: string;
  passwordInput: string;
  loginService: LoginService;

  constructor(loginService: LoginService, private router: Router) {
    this.loginService = loginService;
  }

  ngOnInit(): void {
  }


  submitButtonClick() {
    console.log("before: " + this.loginService.getUserStatus());
    if (this.usernameInput != null) {
      if (this.passwordInput != null) {
        this.loginService.getUserLoginAndPassword(this.usernameInput, this.passwordInput).subscribe((response) => {
          this.loginService.setUserStatus();
          console.log("after: " + this.loginService.getUserStatus());
          //if it gets here, it means the login is successful and we can navigate to a new page
          sessionStorage.setItem('username', this.usernameInput);
          this.router.navigateByUrl(`/home`);
        });
        setTimeout(function () {
          displayInvalidLogin("Invalid Credentials");
        }, 1000);
      } else {
        displayInvalidLogin("Must Enter Password");
      }
    } else {
      displayInvalidLogin("Must Enter Username");
    }

    this.loginService.confirmLogin().subscribe((response) => {
      console.log("confirmed login " + response.username);
    })


  }



}

function clearErrorMessage() {
  let errorMessage = document.getElementById('errorMessage');
  if (errorMessage != null) {
    errorMessage.remove();
  }
}

function displayInvalidLogin(errorString) {
  clearErrorMessage();
  let bodyElement = document.getElementById('checkError');
  let pElement = document.createElement('p');
  pElement.setAttribute("id", "errorMessage");
  pElement.style.color = 'red';
  pElement.innerHTML = errorString;
  bodyElement.appendChild(pElement);
  console.log("invalid request");
}


