package ru.arman.taskflow.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.arman.taskflow.dto.TaskCreateRequest;
import ru.arman.taskflow.dto.TaskResponse;
import ru.arman.taskflow.dto.TaskUpdateRequest;
import ru.arman.taskflow.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")  // Добавили /api/v1 — это REST best practice (версионирование API).
                                   // Если завтра изменится формат ответа, мы сделаем /api/v2
                                   // и старые клиенты не сломаются.
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // GET /api/v1/tasks — получить все задачи
    // Возвращает 200 OK по умолчанию
    @GetMapping
    public List<TaskResponse> getAllTasks() {
        return taskService.getAllTasks();
    }

    // POST /api/v1/tasks — создать задачу
    // @ResponseStatus(HttpStatus.CREATED) — возвращает 201 вместо 200.
    // Это важно! По стандарту HTTP, успешное создание ресурса = 201 Created, а не 200 OK.
    // На собеседовании часто спрашивают: "Какой код ответа при создании?"
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@RequestBody TaskCreateRequest request) {
        return taskService.createTask(request);
    }

    // GET /api/v1/tasks/{id} — получить задачу по ID
    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    // DELETE /api/v1/tasks/{id} — удалить задачу
    // 204 No Content — стандартный ответ при успешном удалении.
    // Тело ответа пустое — удалять нечего показывать.
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    // PUT /api/v1/tasks/{id} — полностью обновить задачу
    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id, @RequestBody TaskUpdateRequest request) {
        return taskService.updateTask(id, request);
    }
}
