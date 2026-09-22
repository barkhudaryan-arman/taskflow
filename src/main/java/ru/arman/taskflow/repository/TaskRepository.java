package ru.arman.taskflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.arman.taskflow.model.Task;

public interface TaskRepository extends JpaRepository<Task,Long> {

}
