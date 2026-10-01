package ru.arman.taskflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank(message = "Имя не может быть пустым")
        @Size(max = 100, message = "Имя не должно превышать 100 символов")
        String name,

        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Некорректный формат email")
        String email,

        @NotBlank(message = "Пароль не может быть пустым")
        @Size(min = 6, message = "пароль должен быть не менее 6 символов")
        String password
) {
}
