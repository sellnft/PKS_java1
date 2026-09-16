package model;

import java.time.OffsetDateTime;

public record Announcement(
        Integer id,
        String category,
        String title,
        String description,
        AnnouncementStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        String comment,
        Integer employeeId,
        Integer userId
) {

    public Announcement{
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Категория должна быть заполнена!");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Заголовок должен быть заполнен!");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Описание должно быть заполнено!");
        }

        if (status == null || status.name().isBlank()) {
            throw new IllegalArgumentException("Статус не может быть пустым!");
        }

        if (userId == null) {
            throw new IllegalArgumentException("ID контрагента не модет быть пустым!");
        }
    }
}
