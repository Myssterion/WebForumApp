import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../service/login.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { environment } from '../../environments/environment.development';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  apiServerUrl = environment.apiServerUrl;
  username: string = '';
  password: string = '';
  verificationCode: string = '';
  loginError: boolean = false;
  codeError: boolean = false;
  isError: boolean = false;
  hasLogged: boolean = false;

  constructor(private loginService: LoginService, private router: Router) { }

  onLogin() {
    this.loginError = false;
    this.codeError = false;
    if (this.hasLogged === false) {
      this.loginService.login(this.username, this.password).subscribe(
        (response: HttpResponse<any>) => { 
          if (response && response.status === 200) {
            this.hasLogged = true;
          }
        }, (error: HttpErrorResponse) => { 
          this.loginError = true;
          this.isError = true;
        }
      )
    }
    else {
      this.loginService.verify(this.username, this.verificationCode).subscribe(
        (response:  HttpResponse<any>) => {
          if (response && response.status === 200) {
            this.loginService.loggedIn = true;
            this.loginService.getCookie();
             this.router.navigate(['/home']);
          }
        },
        (error: HttpErrorResponse) => {
          this.codeError = true;
          this.isError = true;
        }
      )
    }
  }

  onButtonClick() {
      window.location.href = `${this.apiServerUrl}/oauth2/authorization/google`;
    }
}
