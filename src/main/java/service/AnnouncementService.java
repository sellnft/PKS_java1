package service;

import util.ExcelExporter;
import java.io.IOException;
import model.Announcement;
import model.AnnouncementStatus;
import model.User;
import repository.JDBCAnnouncementRepository;
import util.CategoryConfig;
import util.AnnouncementFilter;
import exception.AccessDeniedException;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

public class AnnouncementService {
    private final JDBCAnnouncementRepository jdbcAnnouncementRepository;
    private static final ZoneId zoneId = ZoneId.of("Europe/Moscow");

    public AnnouncementService(JDBCAnnouncementRepository jdbcAnnouncementRepository) {
        this.jdbcAnnouncementRepository = jdbcAnnouncementRepository;
    }

    public void createNewAnnouncement(Integer currentUserID, String category, String title, String description) {
        OffsetDateTime ofd = OffsetDateTime.now(AnnouncementService.zoneId);
        Announcement newAnnouncement = new Announcement(null, category, title, description,
                AnnouncementStatus.PENDING, ofd, null, null, null,
                currentUserID);

        if(!CategoryConfig.checkIfCategoryExists(category)) {
            throw new IllegalArgumentException("Категория не найдена!");
        }

        jdbcAnnouncementRepository.addAnnouncement(newAnnouncement);
    }
    public List<Announcement> getAllAnnouncements() {
        return jdbcAnnouncementRepository.findAll();
    }

    public Optional<Announcement> getAnnouncementByID(int id) {
        return jdbcAnnouncementRepository.getByID(id);
    }

    public List<Announcement> findAnnouncementsByStatus(AnnouncementStatus status) {return jdbcAnnouncementRepository.getAnnouncementsByStatus(status);}

    public boolean setEmployeeForAnnouncement(int id, User currentUser) {
        if (!currentUser.role().isEmployee() && !currentUser.role().isAdmin()) {
            throw new AccessDeniedException("Этот функционал доступен только сотрудникам");
        }
        OffsetDateTime timeNow = OffsetDateTime.now(AnnouncementService.zoneId);
        Timestamp timestamp = Timestamp.from(timeNow.toInstant());
        boolean isNewStatusSet = jdbcAnnouncementRepository.setNewAnnouncementStatus(id, AnnouncementStatus.IN_PROCESS);
        boolean isEmployeeSet = jdbcAnnouncementRepository.setEmployeeForAnnouncement(id, currentUser.id());
        boolean isTimeUpdated = jdbcAnnouncementRepository.setUpdateAtAnnouncement(id, timestamp);

        return isNewStatusSet && isEmployeeSet && isTimeUpdated;
    }

    public boolean DoneAnnouncement(int id, String comment, User currentUser) {
        if (!currentUser.role().isEmployee() && !currentUser.role().isAdmin()) {
            throw new AccessDeniedException("Этот функционал доступен только сотрудникам");
        }
        OffsetDateTime timeNow = OffsetDateTime.now(AnnouncementService.zoneId);
        Timestamp timestamp = Timestamp.from(timeNow.toInstant());
        boolean isDoneStatusSet = jdbcAnnouncementRepository.setNewAnnouncementStatus(id, AnnouncementStatus.DONE);
        boolean isCommentSet = jdbcAnnouncementRepository.setCommentToAnnouncement(id, comment);
        boolean isTimeUpdated = jdbcAnnouncementRepository.setUpdateAtAnnouncement(id, timestamp);

        return isDoneStatusSet && isCommentSet && isTimeUpdated;
    }

    public List<Announcement> getAllAnnouncementsOfUser(int userId) {
        return jdbcAnnouncementRepository.getAllAnnouncementsOfUser(userId);
    }

    public boolean cancelAnnouncement(int id, User currentUser) {
        if (!currentUser.role().isAdmin()) {
            throw new AccessDeniedException("Этот функционал доступен только администраторам системы");
        }
        OffsetDateTime timeNow = OffsetDateTime.now(AnnouncementService.zoneId);
        Timestamp timestamp = Timestamp.from(timeNow.toInstant());

        return jdbcAnnouncementRepository.cancelAnnouncement(id) && jdbcAnnouncementRepository.setUpdateAtAnnouncement(id, timestamp);
    }

    public List<Announcement> findAnnouncementsByFilter(AnnouncementFilter filter) {
        return jdbcAnnouncementRepository.findByFilter(filter);
    }
    public boolean deleteAnnouncement(int id, User currentUser) {
        if (!currentUser.role().isAdmin()) {
            throw new AccessDeniedException("Этот функционал доступен только администраторам системы");
        }
        return jdbcAnnouncementRepository.deleteAnnouncement(id);
    }

    public void exportAllToExcel(String filePath) {
        List<Announcement> all = getAllAnnouncements();
        try {
            ExcelExporter.writeAnnouncements(all, filePath);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить Excel: " + e.getMessage(), e);
        }
    }
}
