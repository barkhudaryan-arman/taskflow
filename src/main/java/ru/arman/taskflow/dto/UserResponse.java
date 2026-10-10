package ru.arman.taskflow.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        String email,
        LocalDateTime createdAt
) implements Serializable {
}
