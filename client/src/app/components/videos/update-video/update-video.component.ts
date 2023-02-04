import { Component, OnInit } from '@angular/core';
import { VideoService } from 'src/app/_services/video.service';
import { Video } from 'src/app/models/video';
import { ActivatedRoute, Router } from '@angular/router';
import { EVisibility } from 'src/app/models/evisibility';

@Component({
  selector: 'app-update-video',
  templateUrl: './update-video.component.html',
  styleUrls: ['./update-video.component.css']
})
export class UpdatevideoComponent implements OnInit {
  id: string;
video_title: string;
visibilityEnum = EVisibility;
video: File = new File([], '');
visibility: string;

  constructor(private videoService: VideoService, private router: Router,
    private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.id = params['videoId'];
      this.video_title = params['video_title'];
      this.visibility = EVisibility[params['visibility']];
    });

  }

  updatevideo() {
    this.videoService.updateVideo(this.id, this.video_title, this.visibility, this.video).subscribe(
        data => {
            console.log(data);
            this.gotoList();
        }
    );
}
onFileChanged(event: any) {
    this.video = new File([event.target.files[0]], event.target.files[0].name);
}
onSubmit() {
    this.updatevideo();
}

gotoList() {
  this.router.navigate(['/video']);
}

}
