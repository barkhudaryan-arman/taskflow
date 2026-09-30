package ru.arman.taskflow.service;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<TaskResponse> getAllTasks() {
        log.info("Получение всех задач");
        List<TaskResponse> tasks = taskRepository.findAll()
                .stream()
                .map(TaskMapper::toResponse)
                .toList();
        log.info("Найдено {} задач", tasks.size());
        return tasks;
    }

    public TaskResponse createTask(TaskCreateRequest request) {
        log.info("Создание задачи: title={}", request.title());
        Task task = TaskMapper.toEntity(request);
        if (request.assigneeId() != null) {
            User assignee = findUserOrThrow(request.assigneeId());
            task.setAssignee(assignee);
            log.debug("Назначен исполнитель: userId={}", request.assigneeId());
        }
        Task savedTask = taskRepository.save(task);
        log.info("Задача создана: id={}", savedTask.getId());
        return TaskMapper.toResponse(savedTask);
    }

    public TaskResponse getTaskById(Long id) {
        log.debug("Поиск задачи по id={}", id);
        Task task = findTaskOrThrow(id);
        return TaskMapper.toResponse(task);
    }

    public void deleteTask(Long id) {
        log.info("Удаление задачи: id={}", id);
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Задача с ID " + id + " не найдена");
        }
        taskRepository.deleteById(id);
        log.info("Задача удалена: id={}", id);
    }

    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {
        log.info("Обновление задачи: id={}", id);
        Task existingTask = findTaskOrThrow(id);
        existingTask.setTitle(request.title());
        existingTask.setDescription(request.description());
        if (request.status() != null) {
            existingTask.setStatus(request.status());
        }
        if (request.assigneeId() != null) {
            User assignee = findUserOrThrow(request.assigneeId());
            existingTask.setAssignee(assignee);
        } else {
            existingTask.setAssignee(null);
        }
        Task updatedTask = taskRepository.save(existingTask);
        log.info("Задача обновлена: id={}", updatedTask.getId());
        return TaskMapper.toResponse(updatedTask);
    }

    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Задача не найдена: id={}", id);
                    return new TaskNotFoundException("Задача с ID " + id + " не найдена");
                });
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Пользователь не найден: id={}", userId);
                    return new UserNotFoundException("Пользователь с ID " + userId + " не найден");
                });
    }
}
