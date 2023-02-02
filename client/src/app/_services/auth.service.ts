import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

const AUTH_API = environment.apiUrl + "/users/";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})

export class AuthService {

  constructor(private http: HttpClient) { }

  login(email: string, password: string): Observable<any> {
    return this.http.post(AUTH_API + 'authenticate',{
      email,
      password
    }, httpOptions ); 
  }

  register(firstName: string, lastName: string, email: string, password:string, role: Number): Observable<any> {
    return this.http.post(AUTH_API, {
      firstName,
      lastName,
      email,
      password,
      role
    }, httpOptions);
  }
}