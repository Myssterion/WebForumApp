import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { Observable } from 'rxjs';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class AccessControlService {

  private apiServerUrl = environment.apiServerUrl; 

  constructor(private http: HttpClient) { }

  getRegisteredUsers() :Observable<User[]> {
      return this.http.get<User[]>(`${this.apiServerUrl}/user/find/registered`);
  }

  approveUser(userId: number) : Observable<HttpResponse<any>> {
    return this.http.post<HttpResponse<any>>(`${this.apiServerUrl}/user/update/approve/${userId}`, null, {observe: "response"});
  }
  banUser(userId: number) : Observable<HttpResponse<any>>{
    return this.http.post<HttpResponse<any>>(`${this.apiServerUrl}/user/update/ban/${userId}`, null, {observe: "response"});
  }
}
