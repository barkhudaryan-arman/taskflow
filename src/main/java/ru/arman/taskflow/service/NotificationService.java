package ru.arman.taskflow.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    @Async
    public void sendTaskCreatedNotification(String email, String taskTitle) {
        log.info("Начинаем отправку письма на {} в потоке: {}", email, Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("Письмо успешно отправлено! Уведомление о задаче: {}", taskTitle);
    }
}
