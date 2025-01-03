import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { CommentsService } from './comments.service';
import { Comment } from '../models/comment';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Permission } from '../models/permission';

declare var bootstrap: any;

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrl: './comments.component.css'
})
export class CommentsComponent implements OnInit {

  @ViewChild('modifyComment') modifyModal!: ElementRef;

  private modalInstance: any;

  public pageSize: number = 20;
  public currentPage: number = 1;
  comments: Comment[] = [];
  public modifyVisible: boolean = false;
  public selectedComment: Comment | null = null;
  type : string = "";

  public canApprove: boolean = false;
  public canModify: boolean = false;
  public canBan: boolean = false;

  constructor(private commentsService: CommentsService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.type = params["type"];
      this.getNewComments(this.type);
      this.checkPermissionForUser();
    })
  }

  checkPermissionForUser() { //izbaci username ako radi
    this.commentsService.checkPermissionForUser(this.type).subscribe(
      (response : Permission) => {
       this.canApprove = response.permissions.includes("APPROVE");
       this.canModify = response.permissions.includes("MODIFY");
       this.canBan = response.permissions.includes("BAN");
      }, (error : HttpErrorResponse) => {
        console.log(error.message);
      }
    )
  }

  getNewComments(topicName: string) {
    this.commentsService.getNewComments(topicName).subscribe(
      (response: Comment[]) => {
        this.comments = response;
      },
      (error: HttpErrorResponse) => {
        console.log("ERROR");
      }
    )
  }

  approveComment(Id: number | null) {
    if (Id != null) { 
      this.commentsService.approveComment(Id).subscribe(
        (response: HttpResponse<any>) => {
          if(response.status === 200) {
            this.comments = this.comments.filter(comm => comm.commentId !== Id);
          }
        },
        (error: HttpErrorResponse) => {
          console.log("ERROR");
        }
      )
    }
  }

  banComment(Id: number | null) {
    if (Id != null) {
      this.commentsService.banComment(Id).subscribe(
        (response: HttpResponse<any>) => {
          this.comments = this.comments.filter(comm => comm.commentId !== Id);
        },
        (error: HttpErrorResponse) => {
          console.log("ERROR");
        }
      )
    }
  }


  modifyComment(Id: number, comment: Comment) {
      this.commentsService.modifyAndApproveComment(Id, comment).subscribe(
        (response: Comment) => {
         this.comments = this.comments.filter(comm => comm.commentId !== Id);
        },
        (error: HttpErrorResponse) => {
          console.log("ERROR");
        }
      )
  }

  saveComment() {
    if(this.selectedComment !== null && this.selectedComment.commentId !== null) {
      this.modifyComment(this.selectedComment.commentId, this.selectedComment);
      this.closeModifyComment();
    }
  }

  showModifyComment(comment: Comment) {
    if(!this.modifyVisible) {
      this.modifyVisible = true;
      this.selectedComment = { ...comment };
      console.log(this.selectedComment);

      if(!this.modalInstance)
      this.modalInstance = new bootstrap.Modal(this.modifyModal.nativeElement, {
        backdrop: 'static', // Prevent closing on outside click
        keyboard: false     // Disable closing with Esc key
      });
      
      this.modalInstance.show();
    }
    }

    closeModifyComment() {
      if(this.modifyVisible ) {
        this.modifyVisible = false;
        this.modalInstance.hide();
        this.selectedComment = null;
      }
    }
}
