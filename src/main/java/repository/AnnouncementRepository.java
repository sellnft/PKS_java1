package repository;

import model.Announcement;
import model.AnnouncementStatus;
import util.AnnouncementFilter;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface AnnouncementRepository {

    void addAnnouncement(Announcement newAnnouncement);
    List<Announcement> findAll();
    Optional<Announcement> getByID(int id);
    List<Announcement> getAnnouncementsByStatus(AnnouncementStatus status);
    List<Announcement> getAllAnnouncementsOfUser(int userId);
    boolean setNewAnnouncementStatus(int id, AnnouncementStatus status);
    boolean setEmployeeForAnnouncement(int id, int employeeId);
    boolean setUpdateAtAnnouncement(int id, Timestamp time);
    boolean setCommentToAnnouncement(int id, String comment);
    List<Announcement> findByFilter(AnnouncementFilter filter);
}
