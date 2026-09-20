package service;

import model.Announcement;
import model.AnnouncementStatus;
import model.CategoryType;
import repository.JDBCAnnouncementRepository;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

public class AnnouncementService {

    private final JDBCAnnouncementRepository jdbcAnnouncementRepository;
    private final ZoneId zoneId = ZoneId.of("Europe/Moscow");

    public AnnouncementService(JDBCAnnouncementRepository jdbcAnnouncementRepository) {
        this.jdbcAnnouncementRepository = jdbcAnnouncementRepository;
    }

    public void createNewAnnouncement(Integer currentUserID, CategoryType category, String title, String description) {
        OffsetDateTime ofd = OffsetDateTime.now(zoneId);
        Announcement newAnnouncement = new Announcement(null, category, title, description,
                AnnouncementStatus.PENDING, ofd, null, null, null,
                currentUserID);

        jdbcAnnouncementRepository.addAnnouncement(newAnnouncement);
    }
    public List<Announcement> getAllAnnouncements() {
        return jdbcAnnouncementRepository.findAll();
    }

    public Optional<Announcement> getAnnouncementByID(int id) {
        return jdbcAnnouncementRepository.getByID(id);
    }

    public List<Announcement> findAnnouncementsByStatus(AnnouncementStatus status) {return jdbcAnnouncementRepository.getAnnouncementsByStatus(status);}

    public boolean setEmployeeForAnnouncement(int id, int employeeID) {
        OffsetDateTime timeNow = OffsetDateTime.now();
        Timestamp timestamp = Timestamp.from(timeNow.toInstant());
        boolean isNewStatusSet = jdbcAnnouncementRepository.setNewAnnouncementStatus(id, AnnouncementStatus.IN_PROCESS);
        boolean isEmployeeSet = jdbcAnnouncementRepository.setEmployeeForAnnouncement(id, employeeID);
        boolean isTimeUpdated = jdbcAnnouncementRepository.setUpdateAtAnnouncement(id, timestamp);

        return isNewStatusSet && isEmployeeSet && isTimeUpdated;
    }

    public boolean DoneAnnouncement(int id, String comment) {
        OffsetDateTime timeNow = OffsetDateTime.now();
        Timestamp timestamp = Timestamp.from(timeNow.toInstant());
        boolean isDoneStatusSet = jdbcAnnouncementRepository.setNewAnnouncementStatus(id, AnnouncementStatus.DONE);
        boolean isCommentSet = jdbcAnnouncementRepository.setCommentToAnnouncement(id, comment);
        boolean isTimeUpdated = jdbcAnnouncementRepository.setUpdateAtAnnouncement(id, timestamp);

        return isDoneStatusSet && isCommentSet && isTimeUpdated;
    }
}
