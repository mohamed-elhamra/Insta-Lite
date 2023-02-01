package com.instalite.api.commons.mappers;

import com.instalite.api.dtos.responses.ImageResponse;
import com.instalite.api.dtos.responses.VideoResponse;
import com.instalite.api.entities.ImageEntity;
import com.instalite.api.entities.VideoEntity;

@org.mapstruct.Mapper(componentModel = "spring")
public abstract class VideoMapper {
    public abstract VideoResponse toVideoResponse(VideoEntity videoEntity);
}
