import { Component } from '@angular/core';
import { EVisibility } from 'src/app/models/evisibility';
import { ImageService } from 'src/app/_services/image.service';

@Component({
  selector: 'app-create-image',
  templateUrl: './create-image.component.html',
  styleUrls: ['./create-image.component.css']
})
export class CreateImageComponent {
    image_title: string;
    image: File;
    visibilityEnum = EVisibility;
    visibility: string;
    constructor(private imageService: ImageService) { }
    uploadImage() {
        this.imageService.uploadImage(this.image_title, this.visibility, this.image).subscribe(
            data => {
                console.log(data);
            }
        );
    }
    onFileChanged(event: any) {
        this.image = new File([event.target.files[0]], event.target.files[0].name);
    }
    onSubmit() {
        this.uploadImage();
    }
}
