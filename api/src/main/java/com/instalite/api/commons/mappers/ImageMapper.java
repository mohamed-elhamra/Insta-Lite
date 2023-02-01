package com.instalite.api.commons.mappers;

import com.instalite.api.dtos.responses.ImageResponse;
import com.instalite.api.entities.ImageEntity;
import org.mapstruct.Mapping;

@org.mapstruct.Mapper(componentModel = "spring")
public abstract class ImageMapper {

    @Mapping(target = "userPublicId", expression = "java(imageEntity.getUser().getPublicId())")
    @Mapping(target = "userFirstName", expression = "java(imageEntity.getUser().getFirstName())")
    @Mapping(target = "userLastName", expression = "java(imageEntity.getUser().getLastName())")
    public abstract ImageResponse toImageResponse(ImageEntity imageEntity);

}
