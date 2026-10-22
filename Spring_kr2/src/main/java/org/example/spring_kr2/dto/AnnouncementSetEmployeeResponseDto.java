package org.example.spring_kr2.dto;

import org.example.spring_kr2.model.AnnouncementEntity;

import java.time.OffsetDateTime;

public record AnnouncementSetEmployeeResponseDto(Integer id, String employeeFio, OffsetDateTime announcementUpdatedAt) {
    public static AnnouncementSetEmployeeResponseDto from(AnnouncementEntity announcement) {
        return new AnnouncementSetEmployeeResponseDto(announcement.getId(), announcement.getEmployee().getFio(), announcement.getUpdatedAt());
    }
}
