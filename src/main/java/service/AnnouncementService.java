package service;

import model.Announcement;
import model.AnnouncementStatus;
import model.CategoryType;
import repository.AnnouncementRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final ZoneId zoneId = ZoneId.of("Europe/Moscow");

    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    public void createNewAnnouncement(Integer currentUserID, CategoryType category, String title, String description) {
        OffsetDateTime ofd = OffsetDateTime.now(zoneId);
        Announcement newAnnouncement = new Announcement(null, category, title, description,
                AnnouncementStatus.PENDING, ofd, null, null, null,
                currentUserID);

        announcementRepository.addAnnouncement(newAnnouncement);
    }
    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    public Optional<Announcement> getAnnouncementByID(int id) {
        return announcementRepository.getByID(id);
    }

    public List<Announcement> findAnnouncementsByStatus(AnnouncementStatus status) {return announcementRepository.getAnnouncementsByStatus(status);}

    public boolean setEmployeeForAnnouncement(int id, int employeeID) {
        OffsetDateTime timeNow = OffsetDateTime.now();
        Timestamp timestamp = Timestamp.from(timeNow.toInstant());
        boolean isNewStatusSet = announcementRepository.setNewAnnouncementStatus(id, AnnouncementStatus.IN_PROCESS);
        boolean isEmployeeSet = announcementRepository.setEmployeeForAnnouncement(id, employeeID);
        boolean isTimeUpdated = announcementRepository.setUpdateAtAnnouncement(id, timestamp);

        return isNewStatusSet && isEmployeeSet && isTimeUpdated;
    }

    public boolean DoneAnnouncement(int id, String comment) {
        OffsetDateTime timeNow = OffsetDateTime.now();
        Timestamp timestamp = Timestamp.from(timeNow.toInstant());
        boolean isDoneStatusSet = announcementRepository.setDoneStatusToAnnouncement(id);
        boolean isCommentSet = announcementRepository.setCommentToAnnouncement(id, comment);
        boolean isTimeUpdated = announcementRepository.setUpdateAtAnnouncement(id, timestamp);

        return isDoneStatusSet && isCommentSet && isTimeUpdated;
    }
}
