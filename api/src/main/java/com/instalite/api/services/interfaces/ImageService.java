package com.instalite.api.services.interfaces;

import com.instalite.api.dtos.responses.ImageResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    ImageResponse uploadImage(String imageTitle, MultipartFile image, Authentication authentication);

}