import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { UserListComponent } from './user-list/user-list.component';
import { UserUpdateComponent } from './user-update/user-update.component';
import { HomeComponent } from './home/home.component';

const routes: Routes = [
  { path: 'create-user', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'users', component:UserListComponent},
  {path: 'update-user/:publicId', component: UserUpdateComponent},
  {path: 'home', component:HomeComponent},
  {path: '', pathMatch: 'full', redirectTo: 'home'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
