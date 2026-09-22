package ru.arman.taskflow.service;

import org.springframework.stereotype.Service;
import ru.arman.taskflow.exception.TaskNotFoundException;
import ru.arman.taskflow.model.Task;
import ru.arman.taskflow.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task crateTask(Task task) {
        return taskRepository.save(task);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с ID " + id + " не найдена!"));
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Задача с ID " + id + " не найдена!");
        }
        taskRepository.deleteById(id);
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task existingTask = getTaskById(id);
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());

        if (updatedTask.getStatus() != null) {
            existingTask.setStatus(updatedTask.getStatus());
        }

        return taskRepository.save(existingTask);
    }
}
