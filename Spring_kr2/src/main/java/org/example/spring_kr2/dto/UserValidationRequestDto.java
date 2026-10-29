package org.example.spring_kr2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserValidationRequestDto(
        @NotBlank(message = "Логин не может быть пустым")
        String login,

        @Email(message = "Email некорректен")
        String email
) {}