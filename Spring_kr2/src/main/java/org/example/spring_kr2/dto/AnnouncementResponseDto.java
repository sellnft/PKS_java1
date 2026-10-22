package org.example.spring_kr2.dto;

import org.example.spring_kr2.model.AnnouncementStatus;
import org.example.spring_kr2.model.AnnouncementEntity;

public record AnnouncementResponseDto(Integer id, String category, String title, String description, AnnouncementStatus status) {
    public static AnnouncementResponseDto from(AnnouncementEntity announcement) {
        return new AnnouncementResponseDto(announcement.getId(), announcement.getCategory(), announcement.getTitle(), announcement.getDescription(), announcement.getStatus());
    }
}
