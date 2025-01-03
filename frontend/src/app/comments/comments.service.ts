import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { Observable } from 'rxjs';
import { Comment } from '../models/comment';
import { Permission } from '../models/permission';

@Injectable({
  providedIn: 'root'
})
export class CommentsService {

  private apiServerUrl = environment.apiServerUrl;

  constructor(private http: HttpClient) {}

  getNewComments(topicName: string) : Observable<Comment[]> {
   return this.http.get<Comment[]>(`${this.apiServerUrl}/comment/find/new/${topicName}`);
  }

  approveComment(commentId: number) : Observable<HttpResponse<any>> {
    return this.http.post<HttpResponse<any>>(`${this.apiServerUrl}/comment/update/approve/${commentId}`, null, {observe: "response"});
   }

   banComment(commentId: number) : Observable<HttpResponse<any>> {
    return this.http.post<HttpResponse<any>>(`${this.apiServerUrl}/comment/update/ban/${commentId}`, null, {observe: "response"});
   }

   modifyAndApproveComment(commentId: number, comment: Comment) : Observable<Comment> { 
    return this.http.put<Comment>(`${this.apiServerUrl}/comment/update/modify/${commentId}`, comment);
   }

   checkPermissionForUser(topicName: string) : Observable<Permission> {
    return this.http.get<Permission>(`${this.apiServerUrl}/user/permission/for/${topicName}`)
  }
}
