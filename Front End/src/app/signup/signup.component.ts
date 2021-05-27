import { Component, OnInit } from '@angular/core';
import { SignupService } from '../signup.service';
import { SignUpUser } from 'src/model/SignUpUser';
import { HttpErrorResponse } from '@angular/common/http';

import { Router } from '@angular/router';
@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  usernameInput: string;
  firstnameInput: string;
  lastnameInput: string;
  emailInput: string;
  passwordInput: string;
  confirmPasswordInput: string;

  signup: SignUpUser;
  signupService: SignupService;


  constructor(signupService: SignupService, private router: Router) {
    this.signupService = signupService;
  }

  ngOnInit(): void {
  }

  signupButtonClick() {
    console.log("signupButtonClick");
    console.log(this.usernameInput);



    if (this.passwordInput == this.confirmPasswordInput) {
      if (this.usernameInput != null) {
        if (this.firstnameInput != null) {
          if (this.lastnameInput != null) {
            if (this.emailInput != null) {
              if (this.passwordInput != null) {
                //Must pass all these checks before information is submitted
                displayInvalidSignUp("Thank you, please wait");

                this.signup = {
                  username: this.usernameInput,
                  password: this.passwordInput,
                  firstName: this.firstnameInput,
                  lastName: this.lastnameInput,
                  email: this.emailInput
                }

                this.signupService.signupUser(this.signup).subscribe((response) => {
                  console.log("response " + response);
                  window.location.href="http://ec2-52-14-217-72.us-east-2.compute.amazonaws.com:8080/project2";
                  //this.router.navigateByUrl(`/login`)
                });

              } else {
                displayInvalidSignUp("Must enter a password");
              }
            } else {
              displayInvalidSignUp("Must enter an email address");
            }
          } else {
            displayInvalidSignUp("Must input a last name")
          }
        } else {
          displayInvalidSignUp("Must input a first name")
        }
      } else {
        displayInvalidSignUp("Must input a username");
      }
    } else {
      displayInvalidSignUp("Passwords must match");
    }



    function clearErrorMessage() {
      let errorMessage = document.getElementById('errorMessage');
      if (errorMessage != null) {
        errorMessage.remove();
      }
    }

    function displayInvalidSignUp(errorString) {
      clearErrorMessage();
      let bodyElement = document.getElementById('checkError');
      let pElement = document.createElement('p');
      pElement.setAttribute("id", "errorMessage");
      pElement.style.color = 'red';
      pElement.innerHTML = errorString;
      bodyElement.appendChild(pElement);
      console.log("invalid request");
    }

  }
}


