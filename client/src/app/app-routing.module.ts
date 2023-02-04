import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { CreateImageComponent } from './components/images/create-image/create-image.component';
import { ListImageComponent } from './components/images/list-image/list-image.component';
import { UpdateImageComponent } from './components/images/update-image/update-image.component';
import { CreatevideoComponent } from './components/videos/create-video/create-video.component';
import { ListvideoComponent } from './components/videos/list-video/list-video.component';
import { UpdatevideoComponent } from './components/videos/update-video/update-video.component';

const routes: Routes = [
  { path: 'create-user', component: RegisterComponent, pathMatch: 'full' },
  { path: 'login', component: LoginComponent, pathMatch: 'full' },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'image/upload', component: CreateImageComponent, pathMatch: 'full' },
  { path: 'image', component: ListImageComponent, pathMatch: 'full' },
  { path: 'image/update', component: UpdateImageComponent, pathMatch: 'full' },
  { path: 'video/upload', component: CreatevideoComponent, pathMatch: 'full' },
  { path: 'video', component: ListvideoComponent, pathMatch: 'full' },
  { path: 'video/update', component: UpdatevideoComponent, pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
