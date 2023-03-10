package com.instalite.api.services;

import com.instalite.api.commons.exceptions.InstaLiteException;
import com.instalite.api.commons.mappers.ImageMapper;
import com.instalite.api.commons.utils.Constants;
import com.instalite.api.commons.utils.IDGenerator;
import com.instalite.api.commons.utils.enums.EVisibility;
import com.instalite.api.controllers.ImageController;
import com.instalite.api.dtos.responses.ImageResponse;
import com.instalite.api.entities.ImageEntity;
import com.instalite.api.entities.UserEntity;
import com.instalite.api.repositories.ImageRepository;
import com.instalite.api.repositories.UserRepository;
import com.instalite.api.services.interfaces.ImageService;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final UserRepository userRepository;
    private final IDGenerator idGenerator;
    private final Path folder;
    private final Environment environment;
    private String host;

    public ImageServiceImpl(ImageRepository imageRepository, ImageMapper imageMapper, UserRepository userRepository, IDGenerator idGenerator, Environment environment) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
        this.userRepository = userRepository;
        this.idGenerator = idGenerator;
        this.environment = environment;
        this.folder = Paths.get("src/main/resources/static/images");
        this.createFolderIfNotExits(this.folder);
        this.instantiateHost();
    }

    @Override
    @Transactional
    public ImageResponse uploadImage(String imageTitle, String visibility, MultipartFile image, Authentication authentication) {
        try{
            UserEntity connectedUser = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User with this email: " + authentication.getName() + " not connected"));
            if(imageRepository.findByTitle(imageTitle).isPresent())
                throw new InstaLiteException("There is already an image with this title : " + imageTitle);
            String imageExtension = StringUtils.getFilenameExtension(image.getOriginalFilename());

            if(Constants.ALLOWED_EXTENSIONS.contains(imageExtension)){
                String imagePublicId = idGenerator.generateStringId();
                String imageName = imagePublicId + "." + imageExtension;
                ImageEntity imageEntity = new ImageEntity(null, imagePublicId, imageTitle, imageName, EVisibility.fromValue(visibility), new Date(), connectedUser);
                if (!Files.exists(folder)) {
                    Files.createDirectories(folder);
                }
                Files.copy(image.getInputStream(), this.folder.resolve(imageName));

                ImageResponse imageResponse = imageMapper.toImageResponse(imageRepository.save(imageEntity));
                imageResponse.setUrl(this.host + imageResponse.getPublicId());
                return imageResponse;
            }else{
                throw new InstaLiteException("File extension allowed (png, jpeg, jpg)");
            }
        }catch(IOException e){
            throw new InstaLiteException("Could not store the image. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource downloadImage(String imageId) {
        try{
            ImageEntity imageEntity = imageRepository.findByPublicId(imageId)
                    .orElseThrow(() -> new RuntimeException("Image not found with this id: " + imageId));
            Path image = this.folder.resolve(imageEntity.getName());
            Resource resource = new UrlResource(image.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new InstaLiteException("Could not read the image with id: " + imageId);
            }
        } catch (MalformedURLException e) {
            throw new InstaLiteException("Error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ImageResponse updateImage(String publicId, String imageTitle, String visibility, MultipartFile image, Authentication authentication) {
        try{
            ImageEntity imageEntity = imageRepository.findByPublicId(publicId)
                    .orElseThrow(() -> new RuntimeException("Image not found with this id: " + publicId));
            if(imageEntity.getUser().getEmail().equals(authentication.getName())){
                if(imageRepository.findByTitle(imageTitle).isPresent() && !imageEntity.getTitle().equals(imageTitle)) {
                    throw new InstaLiteException("There is already an image with this title : " + imageTitle);
                }

                if(imageTitle != null && !imageTitle.isEmpty() && !imageTitle.isBlank())
                    imageEntity.setTitle(imageTitle);
                if(visibility != null && !visibility.isEmpty() && !visibility.isBlank())
                    imageEntity.setVisibility(EVisibility.fromValue(visibility));

                if (image != null && !image.isEmpty() && !image.getOriginalFilename().isEmpty() && !image.getOriginalFilename().isBlank()) {
                    String imageExtension = StringUtils.getFilenameExtension(image.getOriginalFilename());
                    if (Constants.ALLOWED_EXTENSIONS.contains(imageExtension)) {
                        deleteImageFromFolder(imageEntity);
                        Files.copy(image.getInputStream(), this.folder.resolve(imageEntity.getName()));

                    } else {
                        throw new InstaLiteException("File extension allowed (png, jpeg, jpg)");
                    }

                }
                ImageResponse imageResponse = imageMapper.toImageResponse(imageRepository.save(imageEntity));
                imageResponse.setUrl(this.host + imageResponse.getPublicId());
                return imageResponse;
            }else{
                throw new InstaLiteException("You are not allowed to update this image.");
            }
        }catch(IOException e){
            throw new InstaLiteException("Could not store the image. Error: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteImage(String publicId, Authentication authentication) {
        ImageEntity imageEntity = imageRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Image not found with this id: " + publicId));
        if(imageEntity.getUser().getEmail().equals(authentication.getName())){
            deleteImageFromFolder(imageEntity);
            imageRepository.delete(imageEntity);
        }else{
            throw new InstaLiteException("You are not allowed to update this image.");
        }
    }

    @Override
    public List<ImageResponse> listImages(Authentication authentication) {
        List<ImageEntity> images = imageRepository.findAll();
        if(authentication != null && authentication.isAuthenticated()){
            return images.stream()
                    .filter(image -> {
                        boolean isImagePublic = image.getVisibility().getValue().equals(EVisibility.PUBLIC.getValue());
                        boolean isImagePrivate = image.getVisibility().getValue().equals(EVisibility.PRIVATE.getValue());
                        return isImagePublic || isImagePrivate || image.getUser().getEmail().equals(authentication.getName());
                    })
                    .map(imageMapper::toImageResponse)
                    .peek(imageResponse -> imageResponse.setUrl(this.host + imageResponse.getPublicId()))
                    .collect(Collectors.toList());
        }else if(authentication == null){
            return images.stream()
                    .filter(image -> image.getVisibility().getValue().equals(EVisibility.PUBLIC.getValue()))
                    .map(imageMapper::toImageResponse)
                    .peek(imageResponse -> imageResponse.setUrl(this.host + imageResponse.getPublicId()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
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

        this.host = "http://" + address + ":" + port + contextPath + "/images/download/";
    }

    private void deleteImageFromFolder(ImageEntity imageEntity){
        Path imagePath = folder.resolve(imageEntity.getName());
        try{
            Files.delete(imagePath);
        }catch(IOException exception){
            throw new InstaLiteException("Could not delete the image");
        }
    }

}
