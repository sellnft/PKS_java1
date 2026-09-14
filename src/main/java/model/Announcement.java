package model;

import java.time.LocalDateTime;

record Announcement(
        Integer id,
        String category,
        String title,
        String description,
        AnnouncementStatus status,
        LocalDateTime created_at,
        LocalDateTime updated_at,
        String comment,
        Employee employee
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
    public Announcement(String category, String title, String description) {
        this(null, category, title, description, AnnouncementStatus.PENDING, LocalDateTime.now(), null, null, null);
    }
}
