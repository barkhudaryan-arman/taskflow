package ru.arman.taskflow.service;

import org.springframework.stereotype.Service;
import ru.arman.taskflow.dto.TaskCreateRequest;
import ru.arman.taskflow.dto.TaskResponse;
import ru.arman.taskflow.dto.TaskUpdateRequest;
import ru.arman.taskflow.exception.TaskNotFoundException;
import ru.arman.taskflow.exception.UserNotFoundException;
import ru.arman.taskflow.mapper.TaskMapper;
import ru.arman.taskflow.model.Task;
import ru.arman.taskflow.model.User;
import ru.arman.taskflow.repository.TaskRepository;
import ru.arman.taskflow.repository.UserRepository;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(TaskMapper::toResponse)
                .toList();
    }

    public TaskResponse createTask(TaskCreateRequest request) {
        Task task = TaskMapper.toEntity(request);
        Task savedTask = taskRepository.save(task);
        return TaskMapper.toResponse(savedTask);
    }

    public TaskResponse getTaskById(Long id) {
        Task task = findTaskOrThrow(id);
        return TaskMapper.toResponse(task);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Задача с ID " + id + " не найдена");
        }
        taskRepository.deleteById(id);
    }

    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {
        Task existingTask = findTaskOrThrow(id);

        existingTask.setTitle(request.title());
        existingTask.setDescription(request.description());

        if (request.assigneeId() != null) {
            User assignee = findUserOrThrow(request.assigneeId());
            existingTask.setAssignee(assignee);
        }else {
            existingTask.setAssignee(null);
        }
        Task updatedTask = taskRepository.save(existingTask);
        return TaskMapper.toResponse(updatedTask);
    }

    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с ID " + id + " не найдена"));
    }

    private User findUserOrThrow(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID " + userId + " не найден"));
    }
}
