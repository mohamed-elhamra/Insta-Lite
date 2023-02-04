package com.instalite.api.controllers;

import com.instalite.api.dtos.responses.VideoResponse;
import com.instalite.api.dtos.responses.VideoResponse;
import com.instalite.api.services.interfaces.VideoService;
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
@RequestMapping("/videos")
@AllArgsConstructor
public class VideoController {
    private VideoService videoService;


    @PostMapping
    public ResponseEntity<VideoResponse> uploadVideo(@RequestParam(name= "video_title") String videoTitle,
                                                     @RequestParam("visibility") String visibility,
                                                     @RequestParam("video") MultipartFile video,
                                                     Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(videoService.uploadVideo(videoTitle, visibility, video, authentication));
    }

    @GetMapping("/download/{videoId}")
    public ResponseEntity<Resource> downloadVideo(@PathVariable String videoId) {
        Resource video = videoService.downloadVideo(videoId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + video.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
                .body(video);

    }

    @PatchMapping("/{videoId}")
    public ResponseEntity<VideoResponse> updateVideo(@PathVariable(name = "videoId") String videoId,
                                                     @RequestParam(name= "video_title") String videoTitle,
                                                     @RequestParam("visibility") String visibility,
                                                     @RequestParam(value = "video", required = false) MultipartFile video) {
        return ResponseEntity.accepted().body(videoService.updateVideo(videoId, videoTitle, visibility, video));
    }

    @GetMapping
    public ResponseEntity<List<VideoResponse>> listVideos(Authentication authentication){
        return ResponseEntity.ok(videoService.listVideos(authentication));
    }

    @DeleteMapping("/{videoId}")
    public ResponseEntity<String> deleteVideo(@PathVariable(name = "videoId") String videoId, Authentication authentication){
        videoService.deleteVideo(videoId, authentication);
        return ResponseEntity.noContent().build();
    }

}
