import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Image } from '../models/image';

@Injectable({
  providedIn: 'root'
})
export class ImageService {
  private baseUrl = `${environment.apiUrl}/images`;
  constructor(private http: HttpClient) {}

  uploadImage(image_title: string, visibility: string, image: File): Observable<Image> {
    return this.http.post<Image>(`${this.baseUrl}`, {image_title, visibility, image});
  }

  
}
