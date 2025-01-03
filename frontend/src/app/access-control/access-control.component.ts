import { Component, OnInit } from '@angular/core';
import { User } from '../models/user';
import { AccessControlService } from './access-control.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-access-control',
  templateUrl: './access-control.component.html',
  styleUrl: './access-control.component.css'
})
export class AccessControlComponent implements OnInit {

  public pageSize: number = 20;
  public currentPage: number = 1;
  public users: User[] = [];

  constructor(private accessControlService: AccessControlService) { }

  ngOnInit(): void {
    this.getRegisteredUsers();
  }

  getRegisteredUsers() {
    this.accessControlService.getRegisteredUsers().subscribe(
      (response: User[]) => {
        this.users = response;
      },
      (error: HttpErrorResponse) => {
        console.log("ERROR");
      }
    )
  }


  banUser(userId: number | null) {
    if (userId) {
      this.accessControlService.banUser(userId).subscribe(
        (response: HttpResponse<any>) => {
          if (response.status === 200)
            this.users = this.users.filter(user => user.userId !== userId)
        }
      )
    }
  }

  approveUser(userId: number | null) {
    if (userId !== null) {
      console.log("NOT NULLL");
      this.accessControlService.approveUser(userId).subscribe(
        (response: HttpResponse<any>) => {
          if (response.status === 200)
            this.users = this.users.filter(user => user.userId !== userId)
        }
      )
    }
  }

}
