package ru.arman.taskflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.arman.taskflow.model.TaskStatus;

public record TaskUpdateRequest(
        @NotBlank(message = "название задачи не может быть пустым")
        @Size(max = 255, message = "Название задачи не должно превышать 255 символов")
        String title,

        @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
        String description,

        TaskStatus status,

        Long assigneeId
) {
}
