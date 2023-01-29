package com.instalite.api.commons.mappers;

import com.instalite.api.dtos.responses.ImageResponse;
import com.instalite.api.entities.ImageEntity;

@org.mapstruct.Mapper(componentModel = "spring")
public abstract class ImageMapper {

    public abstract ImageResponse toImageResponse(ImageEntity imageEntity);

}
