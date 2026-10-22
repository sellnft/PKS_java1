package org.example.spring_kr2.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.spring_kr2.dto.*;
import org.example.spring_kr2.exception.AnnouncementNotFoundException;
import org.example.spring_kr2.exception.CancelledAnnouncementSetEmployee;
import org.example.spring_kr2.exception.IllegalCategoryException;
import org.example.spring_kr2.exception.UserNotFoundException;
import org.example.spring_kr2.model.AnnouncementEntity;
import org.example.spring_kr2.model.AnnouncementStatus;
import org.example.spring_kr2.model.UserEntity;
import org.example.spring_kr2.repository.AnnouncementRepository;
import org.example.spring_kr2.repository.UserRepository;
import org.example.spring_kr2.util.CategoryConfig;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private static final ZoneId zoneId = ZoneId.of("Europe/Moscow");
    private final UserRepository userRepository;
    private final CategoryConfig categoryConfig;

    @Transactional
    public AnnouncementResponseDto addNewAnnouncement(Integer currentUserID, String category, String title, String description) {
        OffsetDateTime ofd = OffsetDateTime.now(AnnouncementService.zoneId);

        UserEntity author = userRepository.findById(currentUserID).orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        AnnouncementEntity newAnnouncement = new AnnouncementEntity();
        newAnnouncement.setCategory(category);
        newAnnouncement.setTitle(title);
        newAnnouncement.setDescription(description);
        newAnnouncement.setStatus(AnnouncementStatus.PENDING);
        newAnnouncement.setCreatedAt(ofd);
        newAnnouncement.setUser(author);

        if(!categoryConfig.checkIfCategoryExists(newAnnouncement.getCategory())) {
            throw new IllegalCategoryException("Такой категории не существует");
        }

        AnnouncementEntity saved = announcementRepository.save(newAnnouncement);
        return AnnouncementResponseDto.from(saved);
    }

    public AnnouncementDetailResponseDto findAnnouncementById(Integer id) {
        AnnouncementEntity found = announcementRepository.findById(id).orElseThrow(() -> new AnnouncementNotFoundException("Нет заявки с таким ID"));

        return AnnouncementDetailResponseDto.from(found);
    }

    public List<AnnouncementResponseDto> getAllAnnouncements() {
        ArrayList<AnnouncementResponseDto> announcements = new ArrayList<>();

        for (AnnouncementEntity announcement : announcementRepository.findAll()) {
            announcements.add(AnnouncementResponseDto.from(announcement));
        }

        return announcements;
    }

    @Transactional
    public AnnouncementSetEmployeeResponseDto setEmployeeToAnnouncement(Integer id, Integer employeeId) {
        AnnouncementEntity announcement = announcementRepository.findById(id).orElseThrow(() -> new AnnouncementNotFoundException("Нет заявки с таким ID"));
        UserEntity employee = userRepository.findById(employeeId).orElseThrow(() -> new UserNotFoundException("Нет сотрудника с таким ID"));
        OffsetDateTime ofd = OffsetDateTime.now(AnnouncementService.zoneId);

        if(announcement.getStatus().equals(AnnouncementStatus.CANCELLED)) {
            throw new CancelledAnnouncementSetEmployee("Данная заявка не может быть взята в работу, так как она была отменена администратором");
        }

        announcement.setEmployee(employee);
        announcement.setUpdatedAt(ofd);
        announcement.setStatus(AnnouncementStatus.IN_PROGRESS);

        AnnouncementEntity saved = announcementRepository.save(announcement);
        return AnnouncementSetEmployeeResponseDto.from(saved);

    }

    @Transactional
    public AnnouncementDoneStatusDto setDoneStatusToAnnouncement(Integer id) {
        AnnouncementEntity announcement = announcementRepository.findById(id).orElseThrow(() -> new AnnouncementNotFoundException("Заявка с таким ID не найдена"));
        OffsetDateTime ofd = OffsetDateTime.now(AnnouncementService.zoneId);

        announcement.setStatus(AnnouncementStatus.DONE);
        announcement.setUpdatedAt(ofd);

        AnnouncementEntity saved = announcementRepository.save(announcement);
        return AnnouncementDoneStatusDto.from(saved);
    }

    @Transactional
    public AnnouncementCancelledStatusDto setCancelledStatus(Integer id) {
        AnnouncementEntity announcement = announcementRepository.findById(id).orElseThrow(() -> new AnnouncementNotFoundException("Заявка с таким ID не найдена"));
        OffsetDateTime ofd = OffsetDateTime.now(AnnouncementService.zoneId);

        announcement.setUpdatedAt(ofd);
        announcement.setStatus(AnnouncementStatus.CANCELLED);

        AnnouncementEntity saved = announcementRepository.save(announcement);
        return AnnouncementCancelledStatusDto.from(saved);
    }
}
