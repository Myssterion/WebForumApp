import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { LoginService } from './login.service';

@Injectable({
  providedIn: 'root'
})
export class ModeratorGuardService implements CanActivate{

  constructor(private loginService: LoginService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    if (this.loginService.isLoggedin() && (this.loginService.getRole() === "MODERATOR" || this.loginService.getRole() === "ADMIN")) {
      return true;
    } else {
      this.router.navigate(['/home']);
      return false;
    }
  }
}
