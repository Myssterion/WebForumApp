import { Component } from '@angular/core';
import { LoginService } from '../service/login.service';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  token: string = "";

  constructor(private loginService: LoginService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
 //   Promise.resolve().then(() => {
      this.route.queryParams.subscribe(params => {
        this.token = params['temp'];
        if (this.token) {
          this.loginService.retrieveJWT(this.token).subscribe(
            (response: HttpResponse<any>) => {
              if (response.status === 200) {
                this.loginService.notifyLogin();
                this.router.navigate(['/home'])
              }
            }, (error: HttpErrorResponse) => {
              console.log(error.message)
            }
          );
        }
      });
  //  });
  }

}
