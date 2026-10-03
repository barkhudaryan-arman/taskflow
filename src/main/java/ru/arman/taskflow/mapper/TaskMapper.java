package ru.arman.taskflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.arman.taskflow.dto.TaskCreateRequest;
import ru.arman.taskflow.dto.TaskResponse;
import ru.arman.taskflow.model.Task;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TaskMapper {

    Task toEntity(TaskCreateRequest request);

    @Mapping(source = "assignee", target = "assignee")
    TaskResponse toResponse(Task task);
}
