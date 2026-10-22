package org.example.spring_kr2.dto;

import jakarta.validation.constraints.NotBlank;

public record AnnouncementAddedRequestDto(
        @NotBlank(message = "Категория не может быть пустой")
        String category,

        @NotBlank(message = "Заголовок не может быть пустым")
        String title,

        @NotBlank(message = "Описание не может быть пустым")
        String description
) {}