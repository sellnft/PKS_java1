package org.example.spring_kr2.dto;

import org.example.spring_kr2.model.AnnouncementEntity;
import org.example.spring_kr2.model.AnnouncementStatus;

public record AnnouncementDoneStatusDto(Integer id, AnnouncementStatus status) {
    public static AnnouncementDoneStatusDto from(AnnouncementEntity announcement) {
        return new AnnouncementDoneStatusDto(announcement.getId(), announcement.getStatus());
    }
}
