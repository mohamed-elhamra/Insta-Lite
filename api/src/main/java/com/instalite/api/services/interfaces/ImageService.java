package com.instalite.api.services.interfaces;

import com.instalite.api.dtos.responses.ImageResponse;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    ImageResponse uploadImage(String imageTitle, String visibility, MultipartFile image, Authentication authentication);

    Resource downloadImage(String imageId);

    ImageResponse updateImage(String publicId, String imageTitle, String visibility, MultipartFile image, Authentication authentication);

    void deleteImage(String publicId, Authentication authentication);

    List<ImageResponse> listImages(Authentication authentication);
}
