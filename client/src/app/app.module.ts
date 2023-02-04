import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';


import { authInterceptorProviders } from './_helpers/auth.interceptor';
import { AppRoutingModule } from './app-routing.module';
import { CreateImageComponent } from './components/images/create-image/create-image.component';
import { UpdateImageComponent } from './components/images/update-image/update-image.component';
import { ListImageComponent } from './components/images/list-image/list-image.component';
import { CreatevideoComponent } from './components/videos/create-video/create-video.component';
import { UpdatevideoComponent } from './components/videos/update-video/update-video.component';
import { ListvideoComponent } from './components/videos/list-video/list-video.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    CreateImageComponent,
    UpdateImageComponent,
    ListImageComponent,
    CreatevideoComponent,
    UpdatevideoComponent,
    ListvideoComponent
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
