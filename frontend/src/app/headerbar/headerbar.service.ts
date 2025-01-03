import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Topic } from '../models/topic';

@Injectable({
  providedIn: 'root'
})
export class HeaderbarService {

  private apiServerUrl = environment.apiServerUrl;

  constructor(private http: HttpClient) { }

  getTopics() :Observable<Topic[]> {
    return this.http.get<Topic[]>(`${this.apiServerUrl}/topic/find/all`);
  }
}
