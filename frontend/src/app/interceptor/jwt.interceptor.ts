import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { catchError, Observable } from 'rxjs';
import { Router } from '@angular/router';
import { LoginService } from '../service/login.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private router: Router, private loginService: LoginService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
      const modifiedReq = request.clone({
        withCredentials: true,
      });

    return next.handle(modifiedReq).pipe(
      catchError(err => {
        if (err.status === 401) {
            this.loginService.logout();
            this.router.navigate(['/login']); 
        }
        throw err; 
      })
    );
    
  }
}