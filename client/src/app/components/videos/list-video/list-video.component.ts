import { Component, OnInit } from '@angular/core';
import { VideoService } from 'src/app/_services/video.service';
import { Video } from 'src/app/models/video';
import { Router } from '@angular/router';
import * as JSZip from 'jszip';
import * as FileSaver from 'file-saver';
import { HttpClient } from '@angular/common/http';


@Component({
  selector: 'app-list-video',
  templateUrl: './list-video.component.html',
  styleUrls: ['./list-video.component.css']
})
export class ListvideoComponent implements OnInit {

  videos: Video[];

  constructor(private videoService: VideoService, private router: Router, private http: HttpClient) { }

  ngOnInit(): void {
    this.listvideos();
  }

  listvideos() {
    this.videoService.listVideos().subscribe(
      data => {
        this.videos = data;
      }
    );
  }

  createvideo() {
    this.router.navigate(['/video/upload']);
  }

  downloadvideo(url: string) {
    window.open(url);
  }

  updatevideo(videoId: string, video_title: string, visibility: string) {
    this.router.navigate(['/video/update'], {queryParams: {videoId: videoId, video_title: video_title, visibility: visibility}});
  }

  deletevideo(videoId: string) {
    this.videoService.deleteVideo(videoId).subscribe(
      data => {
        console.log(data);
        this.listvideos();
      }
    );
  }

  async downlaoadAllvideos() {
    const zip = new JSZip();
    const promises: any[] = [];
  
    this.videos.forEach(video => {
      promises.push(this.http.get(video.url, { responseType: 'blob' }).toPromise());
    });
  
    const videoBlobs = await Promise.all(promises);
  
    videoBlobs.forEach((blob, index) => {
      zip.file(`video-${index}.jpg`, blob);
    });
  
    const content = await zip.generateAsync({ type: 'blob' });
    FileSaver.saveAs(content, 'videos.zip');
  }

}
