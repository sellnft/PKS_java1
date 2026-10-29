package javafxfrontend.dto;

public record AnnouncementResponseDto(
        Integer id,
        String title,
        String description,
        String category,
        String status,
        String createdAt,
        String comment
) {}