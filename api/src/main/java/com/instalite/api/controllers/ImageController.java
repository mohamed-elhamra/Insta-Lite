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

@RestController
@RequestMapping("/images")
@AllArgsConstructor
public class ImageController {

    private ImageService imageService;


    @PostMapping
    public ResponseEntity<ImageResponse> uploadImage(@RequestParam("image_title") String imageTitle,
                                                     @RequestParam("image") MultipartFile image,
                                                     Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(imageService.uploadImage(imageTitle, image, authentication));
    }

    @GetMapping("/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String imageId) {
        Resource image = imageService.downloadImage(imageId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFilename() + "\"")
                .body(image);
    }


}
