import { Component } from '@angular/core';
import { User } from '../models/user';
import { RegisterService } from './register.service';
import { HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  public user: User = new User();
  public confirmPassword: string = '';

  constructor(private registerService: RegisterService, private router: Router) {}

  register() {
    this.registerService.register(this.user).subscribe(
      (response: HttpResponse<any>) => {
        if(response.status === 200)
          this.router.navigate(['/home']);
      }
    )
  }
}
