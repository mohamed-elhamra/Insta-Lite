import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';


import { authInterceptorProviders } from './_helpers/auth.interceptor';
import { AppRoutingModule } from './app-routing.module';
import { UserListComponent } from './components/users/user-list/user-list.component';
import { UserUpdateComponent } from './components/users/user-update/user-update.component';
import { HomeComponent } from './components/home/home.component';
import { MeteoComponent } from './components/meteo/meteo.component';
import { CreateImageComponent } from './components/images/create-image/create-image.component';
import { UpdateImageComponent } from './components/images/update-image/update-image.component';
import { ListImageComponent } from './components/images/list-image/list-image.component';
import { CreatevideoComponent } from './components/videos/create-video/create-video.component';
import { UpdatevideoComponent } from './components/videos/update-video/update-video.component';
import { ListvideoComponent } from './components/videos/list-video/list-video.component';
import { NewsComponent } from './components/news/news.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    UserListComponent,
    UserUpdateComponent,
    HomeComponent,
    MeteoComponent,
    RegisterComponent,
    CreateImageComponent,
    UpdateImageComponent,
    ListImageComponent,
    CreatevideoComponent,
    UpdatevideoComponent,
    ListvideoComponent,
    NewsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [authInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule { }
