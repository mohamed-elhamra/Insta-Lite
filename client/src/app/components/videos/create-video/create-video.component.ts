import { Component } from '@angular/core';
import { EVisibility } from 'src/app/models/evisibility';
import { VideoService } from 'src/app/_services/video.service';

@Component({
  selector: 'app-create-video',
  templateUrl: './create-video.component.html',
  styleUrls: ['./create-video.component.css']
})
export class CreatevideoComponent {
    video_title: string;
    video: File;
    visibilityEnum = EVisibility;
    visibility: string;
    constructor(private videoService: VideoService) { }
    uploadvideo() {
        this.videoService.uploadVideo(this.video_title, this.visibility, this.video).subscribe(
            data => {
                console.log(data);
            }
        );
    }
    onFileChanged(event: any) {
        this.video = new File([event.target.files[0]], event.target.files[0].name);
    }
    onSubmit() {
        this.uploadvideo();
    }
}
