package org.example.spring_kr2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import model.UserRole;

public record UserRequestRegisterDto(
        @NotBlank(message = "Логин не может быть пустым")
        String login,

        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Email некорректен")
        String email
) {}