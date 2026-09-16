package model;

import util.PasswordHasher;

public record User(
        Integer id,
        String login,
        String passwordHash,
        String fio,
        String email,
        UserRole role
) {

    public User{
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("Логин не может быть пустым!");
        }

        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Почта не может быть пустой");
        }

        if (!email.contains("@")) {
            throw new IllegalArgumentException("Почта некорректна!");
        }

        if (role == null) {
            throw new IllegalArgumentException("Роль некорректна!");
        }

        login = login.trim();
        email = email.trim().toLowerCase();
    }
}
