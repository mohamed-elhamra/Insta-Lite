package com.instalite.api.commons.mappers;


import com.instalite.api.dtos.requests.UserRequest;
import com.instalite.api.dtos.responses.UserResponse;
import com.instalite.api.entities.UserEntity;

@org.mapstruct.Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract UserEntity toUserEntity(UserRequest userRequest);

    public abstract UserResponse toUserResponse(UserEntity userEntity);

}
