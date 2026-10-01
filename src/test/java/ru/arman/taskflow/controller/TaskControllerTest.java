package ru.arman.taskflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.arman.taskflow.dto.TaskCreateRequest;
import ru.arman.taskflow.dto.TaskResponse;
import ru.arman.taskflow.exception.TaskNotFoundException;
import ru.arman.taskflow.model.TaskStatus;
import ru.arman.taskflow.service.TaskService;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private TaskService taskService;

    @Test
    @DisplayName("POST /api/v1/tasks — создание задачи, 201")
    void createTask_shouldReturn201() throws Exception {
        // GIVEN
        TaskCreateRequest request = new TaskCreateRequest("Новая задача", "Описание", null);
        TaskResponse response = new TaskResponse(
                1L, "Новая задача", "Описание",
                TaskStatus.TODO, LocalDateTime.now(), null
        );

        when(taskService.createTask(any())).thenReturn(response);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())           // 201
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Новая задача"))
                .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    @DisplayName("POST /api/v1/tasks — пустой title, 400")
    void createTask_shouldReturn400_whenTitleIsBlank() throws Exception {
        // GIVEN — невалидный запрос
        TaskCreateRequest request = new TaskCreateRequest("", "Описание", null);

        // WHEN & THEN — валидация сработает ДО вызова сервиса
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())        // 400
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("GET /api/v1/tasks/{id} — задача найдена, 200")
    void getTaskById_shouldReturn200() throws Exception {
        // GIVEN
        TaskResponse response = new TaskResponse(
                1L, "Задача", "Описание",
                TaskStatus.IN_PROGRESS, LocalDateTime.now(), null
        );

        when(taskService.getTaskById(1L)).thenReturn(response);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/tasks/1"))
                .andExpect(status().isOk())                // 200
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("GET /api/v1/tasks/{id} — не найдена, 404")
    void getTaskById_shouldReturn404_whenNotFound() throws Exception {
        // GIVEN
        when(taskService.getTaskById(999L))
                .thenThrow(new TaskNotFoundException("Задача с ID 999 не найдена"));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/tasks/999"))
                .andExpect(status().isNotFound())           // 404
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Задача с ID 999 не найдена"));
    }
}