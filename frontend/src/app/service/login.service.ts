import { DOCUMENT } from '@angular/common';
import { Inject, Injectable } from '@angular/core';
import { Observable, Subject, tap } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { environment } from '../../environments/environment.development';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  private apiServerUrl = environment.apiServerUrl;
  public loggedIn: boolean = false;
  private token: string | null = null;
  private loggedInSubject = new Subject<boolean>();
  loggedInNotification$ = this.loggedInSubject.asObservable();

  constructor(private http: HttpClient, @Inject(DOCUMENT) private document: Document,
    private router: Router, private cookieService: CookieService) {
    const token = this.cookieService.get("jwt");
    if (token) {
      this.loggedIn = true;
    }
  }

  notifyLogin() {
    this.loggedInSubject.next(true);
  }

  public getCookie(): void {
    console.log("COOKIE: ");
    console.log(this.cookieService.get("jwt"));
  }

  public isLoggedin(): boolean {
    return this.loggedIn;
  }

  public logout(): void {
    this.loggedIn = false;
    this.cookieService.delete("jwt", "/", "localhost");
    this.router.navigate(['/home']);
  }

  public getRole(): any {
    const token = this.cookieService.get("jwt");
    if (token) {
      const decoded: any = jwtDecode(token);
      const role = decoded.role;
      return role;
    }
  }

  getUsername(): any {
    const token = this.cookieService.get("jwt");
    if (token) {
      const decoded: any = jwtDecode(token);
      const username = decoded.sub;
      return username;
    }
  }

  public login(username: string, password: string): Observable<HttpResponse<any>> {
    console.log(username + "|" + password);
    return this.http.post<HttpResponse<any>>(`${this.apiServerUrl}/auth/login`, {
      username: username,
      password: password
    }, { observe: 'response' })
  }

  public verify(username: string, verificationCode: string): Observable<HttpResponse<any>> {
    return this.http.post<HttpResponse<any>>(`${this.apiServerUrl}/auth/verify`, {
      username: username,
      verificationCode: verificationCode
    }, { observe: 'response' })
      .pipe(
        tap(response => {
          if (response.status === 200 && this.cookieService.check('jwt')) {
            this.loggedIn = true;
          } else {
            this.loggedIn = false;
          }
        }));
  }

  public retrieveJWT(temp: string): Observable<HttpResponse<any>> {
    return this.http.get<Observable<HttpResponse<any>>>(`${this.apiServerUrl}/auth/retrieve/jwt/${temp}`, { observe: 'response' })
      .pipe(
        tap(response => {
          if (response.status === 200 && this.cookieService.check('jwt')) {
            this.loggedIn = true; 
          } else {
            this.loggedIn = false;
          }
        })
      )
  }

}
