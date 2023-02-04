import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { User } from '../models/User';
import { TokenStorageService } from './token-storage.service';


const AUTH_API = environment.apiUrl + "/users/";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient, private tokenStorage: TokenStorageService) { }

  getUsersList(): Observable<any> {
    return this.http.get(`${AUTH_API}`);
  }

  deleteUser(publicId: string): Observable<any> {
    return this.http.delete(`${AUTH_API}${publicId}`, { responseType: 'text' });
  }

  updateUser(publicId: string, value: User): Observable<Object> {
    return this.http.patch(`${AUTH_API}${publicId}`, value, this.tokenStorage.getUser());
  }

  getUser(publicId: string): Observable<any> {
    return this.http.get(`${AUTH_API}${publicId}`);
  }
}
