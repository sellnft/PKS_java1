package org.example.spring_kr2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.spring_kr2.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.example.spring_kr2.service.AnnouncementService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/announcements/")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @PostMapping("/create")
    public ResponseEntity<AnnouncementResponseDto> createNewAnnouncement(@Valid @RequestBody AnnouncementAddedRequestDto request) {
        Integer currentUserId = 1; //TODO: закомментить при появлении реальных данных;

        AnnouncementResponseDto dto = announcementService.addNewAnnouncement(currentUserId, request.category(), request.title(), request.description());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<AnnouncementDetailResponseDto> getAnnouncementById(@PathVariable Integer id) {
        AnnouncementDetailResponseDto dto = announcementService.findAnnouncementById(id);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AnnouncementResponseDto>> getAllAnnouncements() {
        List<AnnouncementResponseDto> announcements = announcementService.getAllAnnouncements();
        return ResponseEntity.status(HttpStatus.OK).body(announcements);
    }

    @PatchMapping("/{id}/setEmployee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<AnnouncementSetEmployeeResponseDto> setEmployee(@PathVariable Integer id, @PathVariable Integer employeeId) {
        AnnouncementSetEmployeeResponseDto dto = announcementService.setEmployeeToAnnouncement(id, employeeId);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PatchMapping("/setDoneStatus/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<AnnouncementDoneStatusDto> setDoneStatus(@PathVariable Integer id) {
        AnnouncementDoneStatusDto dto = announcementService.setDoneStatusToAnnouncement(id);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PatchMapping("/setCancelledStatus/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<AnnouncementCancelledStatusDto> setCancelStatus(@PathVariable Integer id) {
        AnnouncementCancelledStatusDto dto = announcementService.setCancelledStatus(id);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

}
