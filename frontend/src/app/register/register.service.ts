import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { User } from '../models/user';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  private apiServerUrl = environment.apiServerUrl;

  constructor(private http: HttpClient) { }

  register(user: User) :Observable<HttpResponse<any>> {
    return this.http.post<HttpResponse<any>>(`${this.apiServerUrl}/auth/register`, user, {observe: "response"});
  }
}
