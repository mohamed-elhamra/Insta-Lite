import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Video } from '../models/video';

@Injectable({
  providedIn: 'root'
})
export class VideoService {

  constructor(private http: HttpClient) { }
  
  
  private baseUrl = `${environment.apiUrl}/videos`;

  uploadVideo(video_title: string, visibility: string, video: File): Observable<Video> {
    const formData = new FormData();
  formData.append('video_title', video_title);
  formData.append('visibility', visibility);
  formData.append('video', video);
    return this.http.post<Video>(`${this.baseUrl}`, formData);
  }

  listVideos(): Observable<Video[]> {
    return this.http.get<Video[]>(`${this.baseUrl}`);
  }

  updateVideo(videoId: string, video_title: string, visibility: string, video: File): Observable<Video> {
    const formData = new FormData();
  formData.append('video_title', video_title);
  formData.append('visibility', visibility);
  formData.append('video', video);
    return this.http
    .patch<Video>(`${this.baseUrl}/${videoId}`, formData);
  }
  
  deleteVideo(videoId: string) {
    return this.http.delete(`${this.baseUrl}/${videoId}`);
  }
}
