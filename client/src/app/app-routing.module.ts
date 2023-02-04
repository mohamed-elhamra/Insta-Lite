import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from 'src/app/components/register/register.component';
import { LoginComponent } from 'src/app/components/login/login.component';
import { UserListComponent } from 'src/app/components/users/user-list/user-list.component';
import { UserUpdateComponent } from 'src/app/components/users/user-update/user-update.component';
import { HomeComponent } from 'src/app/components/home/home.component';
import { MeteoComponent } from 'src/app/components/meteo/meteo.component';
import { CreateImageComponent } from './components/images/create-image/create-image.component';
import { ListImageComponent } from './components/images/list-image/list-image.component';
import { UpdateImageComponent } from './components/images/update-image/update-image.component';
import { CreatevideoComponent } from './components/videos/create-video/create-video.component';
import { ListvideoComponent } from './components/videos/list-video/list-video.component';
import { UpdatevideoComponent } from './components/videos/update-video/update-video.component';
import { NewsComponent } from './components/news/news.component';

const routes: Routes = [
  { path: 'create-user', component: RegisterComponent, pathMatch: 'full' },
  { path: 'login', component: LoginComponent, pathMatch: 'full' },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'image/upload', component: CreateImageComponent, pathMatch: 'full' },
  { path: 'image', component: ListImageComponent, pathMatch: 'full' },
  { path: 'image/update', component: UpdateImageComponent, pathMatch: 'full' },
  { path: 'video/upload', component: CreatevideoComponent, pathMatch: 'full' },
  { path: 'video', component: ListvideoComponent, pathMatch: 'full' },
  { path: 'video/update', component: UpdatevideoComponent, pathMatch: 'full'},
  { path: 'users', component:UserListComponent, pathMatch: 'full'},
  {path: 'update-user/:publicId', component: UserUpdateComponent, pathMatch: 'full'},
  {path: 'home', component:HomeComponent, pathMatch: 'full'},
  {path: '', redirectTo: 'home', pathMatch: 'full'},
  {path: 'meteo', component: MeteoComponent, pathMatch: 'full' },
  {path: 'news', component: NewsComponent, pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
