package ru.arman.taskflow.dto;
import ru.arman.taskflow.model.TaskStatus;
import java.time.LocalDateTime;


public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        LocalDateTime createdAt
) {
}
