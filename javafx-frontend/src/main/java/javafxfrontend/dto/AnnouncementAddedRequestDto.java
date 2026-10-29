package javafxfrontend.dto;

public record AnnouncementAddedRequestDto(
        String category,
        String title,
        String description
) {}