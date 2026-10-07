package ru.arman.taskflow.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.arman.taskflow.dto.TaskCreateRequest;
import ru.arman.taskflow.dto.TaskResponse;
import ru.arman.taskflow.dto.TaskUpdateRequest;
import ru.arman.taskflow.exception.TaskNotFoundException;
import ru.arman.taskflow.exception.UserNotFoundException;
import ru.arman.taskflow.mapper.TaskMapper;
import ru.arman.taskflow.model.Task;
import ru.arman.taskflow.model.TaskStatus;
import ru.arman.taskflow.model.User;
import ru.arman.taskflow.repository.TaskRepository;
import ru.arman.taskflow.repository.UserRepository;
import ru.arman.taskflow.specification.TaskSpecification;


@Slf4j
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, NotificationService notificationService, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.taskMapper = taskMapper;
    }

    @Transactional(readOnly = true)
    public Page<TaskResponse> getAllTasks(TaskStatus status, Long assigneeId, String keyword, Pageable pageable) {
        log.info("Получение задач: status={}, assigneeId={}, keyword={}", status, assigneeId, keyword);

        Specification<Task> spec = Specification
                .where(TaskSpecification.hasStatus(status))
                .and(TaskSpecification.hasAssignee(assigneeId))
                .and(TaskSpecification.titleContains(keyword));

        return taskRepository.findAll(spec, pageable).map(taskMapper::toResponse);
    }

    public TaskResponse createTask(TaskCreateRequest request) {
        log.info("Создание задачи: title={}", request.title());
        Task task = taskMapper.toEntity(request);
        User currentUser = getCurrentUser();
        task.setAssignee(currentUser);
        log.debug("Задача привязана к текущему пользователю: {}", currentUser.getEmail());
        Task savedTask = taskRepository.save(task);
        log.info("Задача создана: id={}", savedTask.getId());
        notificationService.sendTaskCreatedNotification(currentUser.getEmail(), savedTask.getTitle());
        return taskMapper.toResponse(savedTask);
    }

    public TaskResponse getTaskById(Long id) {
        log.debug("Поиск задачи по id={}", id);
        Task task = findTaskOrThrow(id);
        return taskMapper.toResponse(task);
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
        return taskMapper.toResponse(updatedTask);
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

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Текущий пользователь не найден в БД"));
    }
}
