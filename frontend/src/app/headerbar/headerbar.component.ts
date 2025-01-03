import { Component, OnInit } from '@angular/core';
import { LoginService } from '../service/login.service';
import { Topic } from '../models/topic';
import { HeaderbarService } from './headerbar.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-headerbar',
  templateUrl: './headerbar.component.html',
  styleUrl: './headerbar.component.css'
})
export class HeaderbarComponent implements OnInit{

  public topics: Topic[] = [];
  isUser: boolean = false;
  isAdmin: boolean = false;
  isModerator: boolean = false;
  isLoggedIn: boolean = false;

  constructor(public loginService: LoginService, private headerbarService: HeaderbarService) {}

  ngOnInit(): void {
    this.checkIsLoggedIn();

    this.loginService.loggedInNotification$.subscribe(() => {
      this.checkIsLoggedIn();
    });
  }

  getTopics() {
    this.headerbarService.getTopics().subscribe(
      (response: Topic[]) => {
        this.topics = response;
      },
      (error: HttpErrorResponse) => {
        console.log(error.message);
      }
    )
  }

  logout() {
    this.reset();
    this.loginService.logout();
  }

  reset() {
    this.isLoggedIn = false;
    this.isAdmin = false;
    this.isModerator = false;
    this.isUser = false;
  }

  public checkIsLoggedIn() {
    if(this.loginService.isLoggedin()) {
      this.isLoggedIn = true;
      this.getTopics();
      const role = this.loginService.getRole();
      if(role === "USER")
        this.isUser = true;
      else if(role === "MODERATOR")
        this.isModerator = true;
      else if(role === "ADMIN")
        this.isAdmin = true;
    }
  }
}

