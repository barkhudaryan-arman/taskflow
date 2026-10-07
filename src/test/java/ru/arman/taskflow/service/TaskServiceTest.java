package ru.arman.taskflow.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.arman.taskflow.dto.TaskCreateRequest;
import ru.arman.taskflow.dto.TaskResponse;
import ru.arman.taskflow.exception.TaskNotFoundException;
import ru.arman.taskflow.model.Task;
import ru.arman.taskflow.model.TaskStatus;
import ru.arman.taskflow.repository.TaskRepository;
import ru.arman.taskflow.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    @DisplayName("Создание задачи - успешно")
    @Disabled("Нужно обновить под SecurityContext и MapStruct")
    void createTask_shouldReturnTaskResponse() {
        // GIVEN - подготовка данных
        TaskCreateRequest request = new TaskCreateRequest("Тестовая задача", "Описание", null);

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("Тестовая задача");
        savedTask.setDescription("Описание");
        savedTask.setStatus(TaskStatus.TODO);

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskResponse response = taskService.createTask(request);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Тестовая задача");
        assertThat(response.status()).isEqualTo(TaskStatus.TODO);

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Получение задачи по ID - не найдена, бросает исключение")
    void getTaskById_shouldThrowException_whenNotFound() {
        // GIVEN
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(() -> taskService.getTaskById(999L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    @DisplayName("Удаление задачи - успешно")
    void deleteTask_shouldCallRepository() {
        //GIVEN
        when(taskRepository.existsById(1L)).thenReturn(true);

        //WHEN
        taskService.deleteTask(1L);

        //THEN
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Удаление задачи - не найдена")
    void deleteTask_shouldThrowException_whenNotFound() {
        //GIVEN
        when(taskRepository.existsById(999L)).thenReturn(false);

        //WHEN & THEN
        assertThatThrownBy(() -> taskService.deleteTask(999L))
                .isInstanceOf(TaskNotFoundException.class);

        // deleteById не должен был вызваться
        verify(taskRepository, never()).deleteById(any());
    }
}
