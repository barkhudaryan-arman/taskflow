package ru.arman.taskflow.mapper;

import ru.arman.taskflow.dto.TaskCreateRequest;
import ru.arman.taskflow.dto.TaskResponse;
import ru.arman.taskflow.model.Task;


public final class TaskMapper {

    private TaskMapper() {
    }

    public static Task toEntity(TaskCreateRequest request) {
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        return task;
    }

    public static TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt()
        );
    }
}
