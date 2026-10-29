package javafxfrontend.apis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SecureTokenStorage {
    private static final Path TOKEN_FILE = Paths.get("data", "token.txt");

    static {
        try {
            Files.createDirectories(TOKEN_FILE.getParent());
        } catch (IOException e) {
            System.err.println("Не удалось создать папку data");
        }
    }

    public static void saveToken(String token) {
        try {
            Files.writeString(TOKEN_FILE, token);
        } catch (IOException e) {
            System.err.println("Ошибка сохранения токена");
        }
    }

    public static String getToken() {
        try {
            if (Files.exists(TOKEN_FILE)) {
                return Files.readString(TOKEN_FILE);
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения токена");
        }
        return null;
    }

    public static void clearToken() {
        try {
            Files.deleteIfExists(TOKEN_FILE);
        } catch (IOException e) {
            System.err.println("Ошибка удаления токена");
        }
    }
}