package model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;

record User(
        Integer id,
        String login,
        String fio,
        String passwordHash,
        String email,
        List<Announcement> announcements
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

        login = login.trim();
        email = email.trim().toLowerCase();
    }

    public User(String login, String password, String fio, String email) {
        String passwordHash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            passwordHash = HexFormat.of().formatHex(hash);
        } catch(NoSuchAlgorithmException e) {
            throw new IllegalStateException("Алгоритма SHA-256 не найдено");
        }

        this(null, login, fio, passwordHash, email, null);
    }
}
