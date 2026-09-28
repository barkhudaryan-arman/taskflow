package ru.arman.taskflow.service;

import org.springframework.stereotype.Service;
import ru.arman.taskflow.dto.TaskCreateRequest;
import ru.arman.taskflow.dto.TaskResponse;
import ru.arman.taskflow.dto.TaskUpdateRequest;
import ru.arman.taskflow.exception.TaskNotFoundException;
import ru.arman.taskflow.mapper.TaskMapper;
import ru.arman.taskflow.model.Task;
import ru.arman.taskflow.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    // Constructor Injection — правильный способ внедрения зависимостей.
    // Почему не @Autowired на поле?
    // 1. Поле можно сделать final — гарантия иммутабельности.
    // 2. Класс можно легко тестировать: передал мок в конструктор и всё.
    // 3. Spring сам найдёт конструктор и подставит бин (если конструктор один, @Autowired не нужен).
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskResponse> getAllTasks() {
        // Stream API: берём список Entity из БД, преобразуем каждый в DTO через маппер.
        // .stream() — создаёт поток (Stream) из коллекции.
        // .map() — применяет функцию к каждому элементу (здесь: ссылка на метод TaskMapper::toResponse).
        // .toList() — собирает результат обратно в List (Java 16+, неизменяемый список).
        return taskRepository.findAll()
                .stream()
                .map(TaskMapper::toResponse)
                .toList();
    }

    public TaskResponse createTask(TaskCreateRequest request) {
        // 1. Маппим DTO → Entity
        Task task = TaskMapper.toEntity(request);
        // 2. Сохраняем Entity в БД (Hibernate заполнит id и createdAt)
        Task savedTask = taskRepository.save(task);
        // 3. Маппим обратно Entity → Response DTO
        return TaskMapper.toResponse(savedTask);
    }

    public TaskResponse getTaskById(Long id) {
        Task task = findTaskOrThrow(id);
        return TaskMapper.toResponse(task);
    }

    public void deleteTask(Long id) {
        // Проверяем существование перед удалением — чтобы дать понятную ошибку.
        // Без проверки deleteById просто ничего не сделает, и клиент подумает, что всё ок.
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Задача с ID " + id + " не найдена");
        }
        taskRepository.deleteById(id);
    }

    public TaskResponse updateTask(Long id, TaskUpdateRequest request) {
        Task existingTask = findTaskOrThrow(id);

        existingTask.setTitle(request.title());
        existingTask.setDescription(request.description());

        if (request.status() != null) {
            existingTask.setStatus(request.status());
        }

        Task updatedTask = taskRepository.save(existingTask);
        return TaskMapper.toResponse(updatedTask);
    }

    // Приватный метод — вынесли повторяющуюся логику поиска.
    // Принцип DRY (Don't Repeat Yourself): одна и та же логика
    // "найди или брось исключение" использовалась в getTaskById, updateTask и т.д.
    // Вынесли в отдельный метод, чтобы не дублировать.
    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с ID " + id + " не найдена"));
    }
}
