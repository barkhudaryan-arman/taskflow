package ru.arman.taskflow.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public NotificationService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Async
    public void sendTaskCreateNotification(String email, String taskTitle){
        String message = String.format("Новая задача '%s' назначена пользователю '%s'", taskTitle, email);

        kafkaTemplate.send("task-notifications", message);

        log.info("Письмо успешно отправлено в Kafka (топик task-notifications): {}", message);
    }
}
