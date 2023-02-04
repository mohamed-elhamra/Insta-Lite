import { Component, OnInit } from '@angular/core';
import { ImageService } from 'src/app/_services/image.service';
import { Image } from 'src/app/models/image';
import { Router } from '@angular/router';
import * as JSZip from 'jszip';
import * as FileSaver from 'file-saver';
import { HttpClient } from '@angular/common/http';
import { TokenStorageService } from 'src/app/_services/token-storage.service';


@Component({
  selector: 'app-list-image',
  templateUrl: './list-image.component.html',
  styleUrls: ['./list-image.component.css']
})
export class ListImageComponent implements OnInit {
  isLoggedIn = false;
  showAdminBoard = false;
  showUserBoard = false;
  private role:string;
  images: Image[];

  constructor(private imageService: ImageService, private router: Router, private http: HttpClient, private tokenStorageService: TokenStorageService) { }

  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenStorageService.getToken();

    if (this.isLoggedIn) {
      const user = this.tokenStorageService.getUser();
      this.role
       = user.role;

      this.showAdminBoard = this.role.includes('ROLE_ADMIN');
      this.showUserBoard = this.role.includes('ROLE_USER');
    }
    this.listImages();
  }

  listImages() {
    this.imageService.listImages().subscribe(
      data => {
        this.images = data;
      }
    );
  }

  createImage() {
    this.router.navigate(['/image/upload']);
  }

  downloadImage(url: string) {
    window.open(url);
  }

  updateImage(imageId: string, image_title: string, visibility: string) {
    this.router.navigate(['/image/update'], {queryParams: {imageId: imageId, image_title: image_title, visibility: visibility}});
  }

  deleteImage(imageId: string) {
    this.imageService.deleteImage(imageId).subscribe(
      data => {
        console.log(data);
        this.listImages();
      }
    );
  }

  async downlaoadAllImages() {
    const zip = new JSZip();
    const promises: any[] = [];
  
    this.images.forEach(image => {
      promises.push(this.http.get(image.url, { responseType: 'blob' }).toPromise());
    });
  
    const imageBlobs = await Promise.all(promises);
  
    imageBlobs.forEach((blob, index) => {
      zip.file(`image-${index}.jpg`, blob);
    });
  
    const content = await zip.generateAsync({ type: 'blob' });
    FileSaver.saveAs(content, 'images.zip');
  }

}
