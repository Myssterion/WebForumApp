import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { TopicComponent } from './topic/topic.component';
import { HomeComponent } from './home/home.component';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { HeaderbarComponent } from './headerbar/headerbar.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgbPagination } from '@ng-bootstrap/ng-bootstrap';
import { HttpClientModule, provideHttpClient, withInterceptors } from '@angular/common/http';
import { CommentsComponent } from './comments/comments.component';
import { AccessControlComponent } from './access-control/access-control.component';
import { PermissionControlComponent } from './permission-control/permission-control.component';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtInterceptor } from './interceptor/jwt.interceptor';
import {CookieService} from 'ngx-cookie-service';

@NgModule({
  declarations: [
    AppComponent,
    RegisterComponent,
    LoginComponent,
    TopicComponent,
    HomeComponent,
    HeaderbarComponent,
    CommentsComponent,
    AccessControlComponent,
    PermissionControlComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    NgbPagination,
    NgbModule,
    HttpClientModule
  ],
  providers: [
    //provideClientHydration(),
   CookieService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true // Allow multiple interceptors
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
