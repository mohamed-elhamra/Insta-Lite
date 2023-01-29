package com.instalite.api.services;

import com.instalite.api.commons.exceptions.InstaLiteException;
import com.instalite.api.commons.mappers.ImageMapper;
import com.instalite.api.commons.utils.Constants;
import com.instalite.api.commons.utils.IDGenerator;
import com.instalite.api.dtos.responses.ImageResponse;
import com.instalite.api.entities.ImageEntity;
import com.instalite.api.entities.UserEntity;
import com.instalite.api.repositories.ImageRepository;
import com.instalite.api.repositories.UserRepository;
import com.instalite.api.services.interfaces.ImageService;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    public ImageResponse uploadImage(String imageTitle, MultipartFile image, Authentication authentication) {
        try{
            UserEntity connectedUser = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("User with this email: " + authentication.getName() + " not connected"));
            if(imageRepository.findByTitle(imageTitle).isPresent())
                throw new InstaLiteException("There is already an image with this title : " + imageTitle);
            String imageExtension = StringUtils.getFilenameExtension(image.getOriginalFilename());

            if(Constants.ALLOWED_EXTENSIONS.contains(imageExtension)){
                String imagePublicId = idGenerator.generateStringId();
                String imageName = imagePublicId + "." + imageExtension;
                ImageEntity imageEntity = new ImageEntity(null, imagePublicId, imageTitle, imageName, connectedUser);
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
        this.host = "http://" + address + ":" + port + "/api/images/";
    }

}
