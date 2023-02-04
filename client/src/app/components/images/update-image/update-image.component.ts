import { Component, OnInit } from '@angular/core';
import { ImageService } from 'src/app/_services/image.service';
import { Image } from 'src/app/models/image';
import { ActivatedRoute, Router } from '@angular/router';
import { EVisibility } from 'src/app/models/evisibility';

@Component({
  selector: 'app-update-image',
  templateUrl: './update-image.component.html',
  styleUrls: ['./update-image.component.css']
})
export class UpdateImageComponent implements OnInit {
  id: string;
image_title: string;
visibilityEnum = EVisibility;
image: File = new File([], '');
visibility: string;

  constructor(private imageService: ImageService, private router: Router,
    private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.id = params['imageId'];
      this.image_title = params['image_title'];
      this.visibility = EVisibility[params['visibility']];
    });

  }

  updateImage() {
    this.imageService.updateImage(this.id, this.image_title, this.visibility, this.image).subscribe(
        data => {
            console.log(data);
            this.gotoList();
        }
    );
}
onFileChanged(event: any) {
    this.image = new File([event.target.files[0]], event.target.files[0].name);
}
onSubmit() {
    this.updateImage();
}

gotoList() {
  this.router.navigate(['/image']);
}
}
