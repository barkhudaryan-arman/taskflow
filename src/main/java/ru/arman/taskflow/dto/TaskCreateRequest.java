package ru.arman.taskflow.dto;

public record TaskCreateRequest(
        String title,
        String description
) {
}
