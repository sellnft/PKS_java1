package service;

import model.Announcement;
import model.AnnouncementStatus;
import model.User;
import service.UserService;
import repository.AnnouncementRepository;

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

    public void createNewAnnouncement(Integer currentUserID, String category, String title, String description) {
        LocalDateTime ldt = LocalDateTime.now();
        OffsetDateTime ofd = ldt.atZone(zoneId).toOffsetDateTime();
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
}
