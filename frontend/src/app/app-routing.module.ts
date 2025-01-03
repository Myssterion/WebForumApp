import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { TopicComponent } from './topic/topic.component';
import { CommentsComponent } from './comments/comments.component';
import { AccessControlComponent } from './access-control/access-control.component';
import { PermissionControlComponent } from './permission-control/permission-control.component';
import { AdminGuardService } from './service/admin-guard.service';
import { ModeratorGuardService } from './service/moderator-guard.service';

const routes: Routes = [
  {path: 'home', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'comment/:type', component: CommentsComponent, canActivate: [ModeratorGuardService]},
  {path: 'access-control', component: AccessControlComponent, canActivate: [AdminGuardService]},
  {path: 'permission-control', component: PermissionControlComponent, canActivate: [AdminGuardService]},
  {path: 'topic/:type', component: TopicComponent},
  {path: '', redirectTo: "home" , pathMatch: "full"}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
