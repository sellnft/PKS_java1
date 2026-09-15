package model;

import java.time.LocalDateTime;
import model.User;

record Announcement(
        Integer id,
        String category,
        String title,
        String description,
        AnnouncementStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
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
    }
    public Announcement(String category, String title, String description, Integer userId) {
        this(null, category, title, description, AnnouncementStatus.PENDING, LocalDateTime.now(), null, null, null, userId);
    }
}
