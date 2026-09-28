package ru.arman.taskflow.dto;

import ru.arman.taskflow.model.TaskStatus;

public record TaskUpdateRequest(
        String title,
        String description,
        TaskStatus status
) {
}
