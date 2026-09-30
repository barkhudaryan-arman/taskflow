package ru.arman.taskflow.mapper;

import ru.arman.taskflow.dto.UserCreateRequest;
import ru.arman.taskflow.dto.UserResponse;
import ru.arman.taskflow.model.User;

public final class UserMapper {

    public UserMapper() {
    }

    public static User toEntity(UserCreateRequest request){
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        return user;
    }

    public static UserResponse toResponse(User user){
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
