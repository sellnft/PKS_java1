package javafxfrontend.dto;

public record UserRegisterRequestDto(
        String login,
        String password,
        String fio,
        String email
) {}