import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Image } from '../models/image';

@Injectable({
  providedIn: 'root'
})
export class ImageService {
  deleteImage(imageId: string) {
    return this.http.delete(`${this.baseUrl}/${imageId}`);
  }
  private baseUrl = `${environment.apiUrl}/images`;
  constructor(private http: HttpClient) {}

  uploadImage(image_title: string, visibility: string, image: File): Observable<Image> {
    const formData = new FormData();
  formData.append('image_title', image_title);
  formData.append('visibility', visibility);
  formData.append('image', image);
    return this.http.post<Image>(`${this.baseUrl}`, formData);
  }

  listImages(): Observable<Image[]> {
    return this.http.get<Image[]>(`${this.baseUrl}`);
  }

  updateImage(imageId: string, image_title: string, visibility: string, image: File): Observable<Image> {
    const formData = new FormData();
  formData.append('image_title', image_title);
  formData.append('visibility', visibility);
  formData.append('image', image);
    return this.http.patch<Image>(`${this.baseUrl}/${imageId}`, formData);
  }
}
