package ru.arman.taskflow.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryIntegrationTest {
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry){
        // Подключаемся к твоему живому контейнеру Postgres из docker-compose
        registry.add("spring.datasource.url", () -> "jdbc:postgresql://localhost:5433/taskflow");
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () -> "arman20072001");
    }
    @Autowired
    TaskRepository taskRepository;
    @Test
    void testEmptyDatabase(){
        System.out.println("ПОДКЛЮЧИЛИСЬ К POSTGRES ИЗ DOCKER-COMPOSE! 🚀");
        System.out.println("Количество задач в БД: " + taskRepository.findAll().size());

        assertTrue(taskRepository.findAll() != null);
    }
}
