package com.instalite.api.commons.mappers;

import com.instalite.api.dtos.responses.ImageResponse;
import com.instalite.api.dtos.responses.VideoResponse;
import com.instalite.api.entities.ImageEntity;
import com.instalite.api.entities.VideoEntity;
import org.mapstruct.Mapping;

@org.mapstruct.Mapper(componentModel = "spring")
public abstract class VideoMapper {
    @Mapping(target = "userPublicId", expression = "java(videoEntity.getUser().getPublicId())")
    @Mapping(target = "userFirstName", expression = "java(videoEntity.getUser().getFirstName())")
    @Mapping(target = "userLastName", expression = "java(videoEntity.getUser().getLastName())")
    public abstract VideoResponse toVideoResponse(VideoEntity videoEntity);
}
