package org.example.spring_kr2.dto;

import org.example.spring_kr2.model.AnnouncementEntity;
import org.example.spring_kr2.model.AnnouncementStatus;

import java.time.OffsetDateTime;

public record AnnouncementCancelledStatusDto(Integer id, AnnouncementStatus status, OffsetDateTime updatedAt) {
    public static AnnouncementCancelledStatusDto from(AnnouncementEntity announcement) {
        return new AnnouncementCancelledStatusDto(announcement.getId(), announcement.getStatus(), announcement.getUpdatedAt());
    }
}
