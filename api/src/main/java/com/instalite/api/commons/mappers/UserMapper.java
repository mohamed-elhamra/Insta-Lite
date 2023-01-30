package com.instalite.api.commons.mappers;


import com.instalite.api.dtos.requests.UserCreationRequest;
import com.instalite.api.dtos.requests.UserModificationRequest;
import com.instalite.api.dtos.responses.UserResponse;
import com.instalite.api.entities.UserEntity;

import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract UserEntity toUserEntity(UserCreationRequest userCreationRequest);

    public abstract UserEntity toUserEntity(UserModificationRequest userModificationRequest);

    public abstract UserResponse toUserResponse(UserEntity userEntity);

    public abstract List<UserResponse> toUserResponseList(List<UserEntity> userEntities);

}
