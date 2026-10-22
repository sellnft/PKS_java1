package org.example.spring_kr2.dto;

import org.example.spring_kr2.model.AnnouncementEntity;
import org.example.spring_kr2.model.AnnouncementStatus;

import java.time.OffsetDateTime;

public record AnnouncementDetailResponseDto(Integer id, String category, String title, String description, AnnouncementStatus status, OffsetDateTime createdAt, String authorFio, String employeeFio, String comment) {
    public static AnnouncementDetailResponseDto from(AnnouncementEntity announcement) {
        return new AnnouncementDetailResponseDto(announcement.getId(), announcement.getCategory(), announcement.getTitle(), announcement.getDescription(), announcement.getStatus(), announcement.getCreatedAt(), announcement.getUser().getFio(), announcement.getEmployee() != null ? announcement.getEmployee().getFio() : "Не назначен", announcement.getComment() != null ? announcement.getComment() : null);
    }
}
