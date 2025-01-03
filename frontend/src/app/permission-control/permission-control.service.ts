import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment.development';
import { Observable } from 'rxjs';
import { User } from '../models/user';
import { Role } from '../models/role';
import { Permission } from '../models/permission';

@Injectable({
  providedIn: 'root'
})
export class PermissionControlService {

  private apiServerUrl = environment.apiServerUrl;

  constructor(private http: HttpClient) { }

  getUsers() : Observable<User[]> {
    return this.http.get<User[]>(`${this.apiServerUrl}/user/find/all`)
  }

  getRoles() : Observable<Role[]> {
    return this.http.get<Role[]>(`${this.apiServerUrl}/role/find/all`);
  }

  updateRole(userId: number, role: Role) : Observable<Role> {
    return this.http.put<Role>(`${this.apiServerUrl}/user/update/role/${userId}`, role);
  }

  getUsersPermissions(userId: number) : Observable<Permission[]>{
    return this.http.get<Permission[]>(`${this.apiServerUrl}/user/find/permission/${userId}`);
  }

  updateUsersPermissions(userId: number, permissions: Permission[]) : Observable<HttpResponse<any>>{
    return this.http.put<HttpResponse<any>>(`${this.apiServerUrl}/user/update/permission/${userId}`, permissions, {observe: "response"});
  }
}
