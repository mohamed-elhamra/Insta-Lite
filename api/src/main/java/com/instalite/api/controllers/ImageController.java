package com.instalite.api.controllers;

import com.instalite.api.dtos.responses.ImageResponse;
import com.instalite.api.services.interfaces.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/images")
@AllArgsConstructor
public class ImageController {

    private ImageService imageService;


    @PostMapping
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam(name= "image_title") String imageTitle,
                                                     @RequestParam("visibility") String visibility,
                                                     @RequestParam("image") MultipartFile image,
                                                     Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(imageService.uploadImage(imageTitle, visibility, image, authentication));
    }

    @GetMapping("/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String imageId) {
        Resource image = imageService.downloadImage(imageId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFilename() + "\"")
                .body(image);
    }

    @PatchMapping("/{imageId}")
    public ResponseEntity<ImageResponse> updateImage(@PathVariable(name = "imageId") String imageId,
                                                @RequestParam(name= "image_title") String imageTitle,
                                                @RequestParam("visibility") String visibility,
                                                @RequestParam(value = "image", required = false) MultipartFile image,
                                                     Authentication authentication) {
        return ResponseEntity.accepted().body(imageService.updateImage(imageId, imageTitle, visibility, image, authentication));
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<String> deleteImage(@PathVariable(name = "imageId") String imageId, Authentication authentication){
        imageService.deleteImage(imageId, authentication);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ImageResponse>> listImages(Authentication authentication){
        return ResponseEntity.ok(imageService.listImages(authentication));
    }

}
