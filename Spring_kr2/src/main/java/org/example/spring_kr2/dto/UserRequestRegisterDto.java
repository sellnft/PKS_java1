package org.example.spring_kr2.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import model.UserRole;

public record UserRequestRegisterDto(
        @NotBlank(message = "Логин не может быть пустым")
        String login,

        @NotBlank(message = "Пароль не может быть пустым")
        String password,

        @NotBlank(message = "ФИО не может быть пустым")
        String fio,

        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Email некорректен")
        String email,

        @NotNull(message = "Роль не может быть пустой")
        UserRole role

) {}
