package com.instalite.api.services;

import com.instalite.api.commons.exceptions.InstaLiteException;
import com.instalite.api.commons.mappers.VideoMapper;
import com.instalite.api.commons.utils.Constants;
import com.instalite.api.commons.utils.IDGenerator;
import com.instalite.api.commons.utils.enums.EVisibility;
import com.instalite.api.dtos.responses.VideoResponse;
import com.instalite.api.entities.VideoEntity;
import com.instalite.api.entities.UserEntity;
import com.instalite.api.repositories.VideoRepository;
import com.instalite.api.repositories.UserRepository;
import com.instalite.api.repositories.VideoRepository;
import com.instalite.api.services.interfaces.VideoService;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;
    private final UserRepository userRepository;
    private final IDGenerator idGenerator;
    private final Path folder;
    private final Environment environment;
    private String host;

    public VideoServiceImpl(VideoRepository videoRepository, VideoMapper videoMapper, UserRepository userRepository, IDGenerator idGenerator, Environment environment) {
        this.videoRepository = videoRepository;
        this.videoMapper = videoMapper;
        this.userRepository = userRepository;
        this.idGenerator = idGenerator;
        this.environment = environment;
        this.folder = Paths.get("src/main/resources/static/videos");
        this.createFolderIfNotExits(this.folder);
        this.instantiateHost();
    }

    @Override
    @Transactional
    public VideoResponse uploadVideo(String videoTitle, String visibility, MultipartFile video, Authentication authentication) {
        try{
            UserEntity connectedUser = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User with this email: " + authentication.getName() + " not connected"));
            if(videoRepository.findByTitle(videoTitle).isPresent())
                throw new InstaLiteException("There is already an video with this title : " + videoTitle);
            String videoExtension = StringUtils.getFilenameExtension(video.getOriginalFilename());

            if(Constants.ALLOWED_VIDEO_EXTENSIONS.contains(videoExtension)){
                String videoPublicId = idGenerator.generateStringId();
                String videoName = videoPublicId + "." + videoExtension;
                VideoEntity videoEntity = new VideoEntity(null, videoPublicId, videoTitle, videoName, EVisibility.fromValue(visibility), connectedUser);
                if (!Files.exists(folder)) {
                    Files.createDirectories(folder);
                }
                Files.copy(video.getInputStream(), this.folder.resolve(videoName));
                VideoResponse videoResponse = videoMapper.toVideoResponse(videoRepository.save(videoEntity));
                videoResponse.setUrl(this.host + videoResponse.getPublicId());
                return videoResponse;
            }else{
                throw new InstaLiteException("File extension allowed (png, jpeg, jpg)");
            }
        }catch(IOException e){
            throw new InstaLiteException("Could not store the video. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource downloadVideo(String videoId) {
        try{
            VideoEntity videoEntity = videoRepository.findByPublicId(videoId)
                    .orElseThrow(() -> new RuntimeException("Video not found with this id: " + videoId));
            Path video = this.folder.resolve(videoEntity.getName());
            Resource resource = new UrlResource(video.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new InstaLiteException("Could not read the video with id: " + videoId);
            }
        } catch (MalformedURLException e) {
            throw new InstaLiteException("Error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public VideoResponse updateVideo(String publicId, String videoTitle, String visibility, MultipartFile video) {
        try{
            VideoEntity videoEntity = videoRepository.findByPublicId(publicId)
                    .orElseThrow(() -> new RuntimeException("Video not found with this id: " + publicId));
            if(videoRepository.findByTitle(videoTitle).isPresent() && !videoEntity.getTitle().equals(videoTitle))
                throw new InstaLiteException("There is already an video with this title : " + videoTitle);

            String videoExtension = StringUtils.getFilenameExtension(video.getOriginalFilename());

            if(Constants.ALLOWED_VIDEO_EXTENSIONS.contains(videoExtension)){
                videoEntity.setTitle(videoTitle);
                videoEntity.setVisibility(EVisibility.fromValue(visibility));
                deleteVideoFromFolder(videoEntity);
                Files.copy(video.getInputStream(), this.folder.resolve(videoEntity.getName()));
                VideoResponse videoResponse = videoMapper.toVideoResponse(videoRepository.save(videoEntity));
                videoResponse.setUrl(this.host + videoResponse.getPublicId());
                return videoResponse;
            }else{
                throw new InstaLiteException("File extension allowed (png, jpeg, jpg)");
            }
        }catch(IOException e){
            throw new InstaLiteException("Could not store the video. Error: " + e.getMessage());
        }
    }

    private void createFolderIfNotExits(Path folder){
        boolean isFolderCreated = false;
        if(!folder.toFile().exists()) {
            isFolderCreated = folder.toFile().mkdir();
        }
        if(isFolderCreated) System.out.println("Folder created");
    }

    private void instantiateHost(){
        String port = environment.getProperty("server.port");
        String address = environment.getProperty("server.address");
        String contextPath = environment.getProperty("server.servlet.context-path");

        this.host = "http://" + address + ":" + port + contextPath + "/videos/download/";
    }

    private void deleteVideoFromFolder(VideoEntity videoEntity){
        Path videoPath = folder.resolve(videoEntity.getName());
        try{
            Files.delete(videoPath);
        }catch(IOException exception){
            throw new InstaLiteException("Could not delete the video");
        }
    }
}
