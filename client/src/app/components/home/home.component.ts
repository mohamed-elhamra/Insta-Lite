import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ImageService } from 'src/app/_services/image.service';
import { TokenStorageService } from 'src/app/_services/token-storage.service';
import { VideoService } from 'src/app/_services/video.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  isLoggedIn = false;
  showAdminBoard = false;
  showUserBoard = false;
  private role:string;
  posts: any[] = [];
  weekend: string;

  constructor(private imageService: ImageService, private videoService: VideoService, private http: HttpClient,
    private tokenStorageService: TokenStorageService) { }



  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenStorageService.getToken();

    if (this.isLoggedIn) {
      const user = this.tokenStorageService.getUser();
      this.role
       = user.role;

      this.showAdminBoard = this.role.includes('ROLE_ADMIN');
      this.showUserBoard = this.role.includes('ROLE_USER');
    }
    this.http.get('https://cors-anywhere.herokuapp.com/https://estcequecestbientotleweekend.fr/')
  .subscribe(data => {
  },
  error => {
    let extractedText = error.error.text.match(/C&#039;est le week-end ! 🎉/g);

let replacedText = extractedText[0].replace(/&#039;/g, "'");

this.weekend = replacedText; // Output: "C'est le week-end ! 🎉"
  });

    this.imageService.listImages().subscribe(
      data => {
        for (let image of data) {
          let newImage = { ...image, type: "image" };
          this.posts.push(newImage);
        }

        this.posts.sort((a, b) => {
          return <any>new Date(b.createdAt) - <any>new Date(a.createdAt);
        });
      },
      error => {
        console.log(error);
      }
    );
    this.videoService.listVideos().subscribe(
      data => {
        for (let video of data) {
          let newVideo = { ...video, type: "video" };
          this.posts.push(newVideo);
        }

        this.posts.sort((a, b) => {
          return <any>new Date(b.createdAt) - <any>new Date(a.createdAt);
        });
      },
      error => {
        console.log(error);
      }
    );
  }



}
