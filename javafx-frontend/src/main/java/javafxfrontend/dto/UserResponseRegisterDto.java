package javafxfrontend.dto;

public record UserResponseRegisterDto(
        Integer id,
        String login,
        String fio,
        String email,
        String role
) {}