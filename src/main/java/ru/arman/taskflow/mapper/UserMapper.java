package ru.arman.taskflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.arman.taskflow.dto.UserCreateRequest;
import ru.arman.taskflow.dto.UserResponse;
import ru.arman.taskflow.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    User toEntity(UserCreateRequest request);

    UserResponse toResponse(User user);

}
