package ru.arman.taskflow.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskNotificationListener {

    @KafkaListener(topics = "task-notifications", groupId = "taskflow-group")
    public void listen(String message){
        log.info("📥 [ПОЛУЧЕНО ИЗ KAFKA]: {}", message);
        log.info("📧 Имитация отправки реального email пользователю...");
    }
}
