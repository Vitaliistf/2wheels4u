package org.vitaliistf.twowheels4u.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vitaliistf.twowheels4u.dto.request.UserRequestDto;
import org.vitaliistf.twowheels4u.dto.response.UserResponseDto;
import org.vitaliistf.twowheels4u.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "role", target = "role")
    UserResponseDto toDto(User user);

    @Mapping(ignore = true, target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(ignore = true, target = "telegramId")
    @Mapping(ignore = true, target = "role")
    User toModel(UserRequestDto dto);
}
