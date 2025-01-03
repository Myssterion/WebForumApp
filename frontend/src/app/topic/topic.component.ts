import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TopicService } from './topic.service';
import { Comment } from '../models/comment';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { LoginService } from '../service/login.service';
import { Permission } from '../models/permission';

@Component({
  selector: 'app-topic',
  templateUrl: './topic.component.html',
  styleUrl: './topic.component.css'
})
export class TopicComponent implements OnInit{

  public pageSize: number = 20;
  public currentPage: number = 1;
  public comments: Comment[] = [];
  public newComment: string = "";
  public hasContent: boolean = false;
  public canPost: boolean = false;
  type: string = "";

  constructor(private route: ActivatedRoute, private topicService: TopicService, private loginService: LoginService) { }

  ngOnInit(): void {
   this.route.params.subscribe(params => {
    this.type = params["type"];
    this.getTopicComments(this.type);
    this.checkPermissionForUser();
   })
  }

  checkPermissionForUser() { 
    this.topicService.checkPermissionForUser(this.type).subscribe(
      (response : Permission) => {
       this.canPost = response.permissions.includes("POST");
      }, (error : HttpErrorResponse) => {
        console.log(error.message);
      }
    )
  }

  getTopicComments(topicName: string) {
    this.topicService.getTopicComments(topicName).subscribe(
      (response: Comment[]) => {
        this.comments = response;
      },
      (error: HttpErrorResponse) => {
        console.log(error.message)
      }
    );
  }

  checkContent() {
   if(this.newComment !== "")
      this.hasContent = true;
    else
      this.hasContent = false;
  }

  onPostClick() {
    this.topicService.postComment(this.type, this.newComment).subscribe(
      (response: HttpResponse<any>) => {
        if(response && response.status === 200) {
          this.newComment = "";
          this.checkContent();
        }
      }
    ), (error: HttpErrorResponse) => {
      console.log(error.message);
    }
    }
}
