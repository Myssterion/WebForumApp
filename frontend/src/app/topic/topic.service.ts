import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { Observable } from 'rxjs';
import { Comment } from '../models/comment';
import { LoginService } from '../service/login.service';
import { Permission } from '../models/permission';

@Injectable({
  providedIn: 'root'
})
export class TopicService {

  private apiServerUrl = environment.apiServerUrl;

  constructor(private http: HttpClient, private loginService: LoginService) {}

  getTopicComments(topicName: string) : Observable<Comment[]> {
   return this.http.get<Comment[]>(`${this.apiServerUrl}/comment/find/topic/${topicName}`)
  }

  postComment(topic: string, newComment: string) : Observable<HttpResponse<any>> {
    return this.http.post<HttpResponse<any>>(`${this.apiServerUrl}/comment/post/new/${topic}`, {
      content: newComment,
      username: this.loginService.getUsername()
    }, {observe: 'response'})
  }

  checkPermissionForUser(topicName: string) : Observable<Permission> {
    return this.http.get<Permission>(`${this.apiServerUrl}/user/permission/for/${topicName}`)
  }

}
