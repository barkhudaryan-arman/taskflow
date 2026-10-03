package ru.arman.taskflow.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest (
    @NotBlank(message = "Email не должен быть пустым")
    String email,

    @NotBlank(message = "Пароль не должен быть пустым")
    String password
) {
}
