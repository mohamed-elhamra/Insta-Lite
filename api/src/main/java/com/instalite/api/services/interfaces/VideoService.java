package com.instalite.api.services.interfaces;

import com.instalite.api.dtos.responses.ImageResponse;
import com.instalite.api.dtos.responses.VideoResponse;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {
    VideoResponse uploadVideo(String videoTitle, String visibility, MultipartFile video, Authentication authentication);

    Resource downloadVideo(String videoId);

    VideoResponse updateVideo(String publicId, String videoTitle, String visibility, MultipartFile video);

    List<VideoResponse> listVideos(Authentication authentication);

    void deleteVideo(String publicId, Authentication authentication);
}
