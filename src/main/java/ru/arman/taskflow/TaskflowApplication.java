package ru.arman.taskflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.arman.taskflow.model.Project;
import ru.arman.taskflow.model.Task;
import ru.arman.taskflow.model.User;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class TaskflowApplication {

    public static void main(String[] args) {
       // SpringApplication.run(TaskflowApplication.class, args);
        Set<User> users = new HashSet<>();
        User user1 = new User("Arman","test@mail.com");
        User user2 = new User("Max", "test@mail.com");
        users.add(user1);
        users.add(user2);
        System.out.println("Количество пользователей: " + users.size());
        Project project = new Project("Кино");
        Task task1 = new Task("Запуск фильма","проследовать за порядком");
        Task task2 = new Task("Конец фильма", "очистка помещения");
        project.addTask(task1);
        project.addTask(task2);
        System.out.println("Название проекта " + project + " количество задач в нем " + project.getTasks());
    }

}
