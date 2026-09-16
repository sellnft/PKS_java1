import model.Announcement;
import model.User;
import model.UserRole;
import repository.AnnouncementRepository;
import repository.UserRepository;
import service.AnnouncementService;
import service.UserService;

import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = new UserService(new UserRepository());
    private static final AnnouncementService announcementService =
            new AnnouncementService(new AnnouncementRepository());

    // Кто сейчас залогинен. null = гость.
    private static User currentUser = null;

    public static void main(String[] args) {
        while (true) {
            if (currentUser == null) {
                guestLoop();
            } else {
                userLoop();
            }
        }
    }

    // ---------- Гость ----------

    private static void guestLoop() {
        printGuestMenu();
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> registerUserFlow();
            case "2" -> loginUserFlow();
            case "0" -> {
                System.out.println("Выход.");
                System.exit(0);
            }
            default -> System.out.println("Неизвестная команда.\n");
        }
    }

    private static void printGuestMenu() {
        System.out.println("""
                ==== Helpdesk ====
                [1] Регистрация
                [2] Вход
                [0] Выход
                """);
        System.out.print("> ");
    }

    private static void registerUserFlow() {
        System.out.println("\n--- Регистрация ---");

        System.out.print("Логин: ");
        String login = scanner.nextLine();

        System.out.print("Пароль: ");
        String password = scanner.nextLine();

        System.out.print("ФИО: ");
        String fio = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        UserRole role = askRole();

        try {
            userService.registerUser(login, password, fio, email, role);
            System.out.println("✅ Пользователь создан\n");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage() + "\n");
        } catch (RuntimeException e) {
            System.out.println("💥 Ошибка БД: " + e.getMessage() + "\n");
        }
    }

    private static void loginUserFlow() {
        System.out.println("\n--- Вход ---");

        System.out.print("Логин: ");
        String login = scanner.nextLine();

        System.out.print("Пароль: ");
        String password = scanner.nextLine();

        Optional<User> logged = userService.loginUser(login, password);

        if (logged.isPresent()) {
            currentUser = logged.get();
            System.out.println("✅ Добро пожаловать, " + currentUser.fio() + "!\n");
        } else {
            System.out.println("❌ Неверный логин или пароль\n");
        }
    }

    // ---------- Залогиненный пользователь ----------

    private static void userLoop() {
        printUserMenu();
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> System.out.println("TODO: мои заявки\n");
            case "2" -> createAnnouncementFlow();
            case "9" -> logout();
            case "0" -> {
                System.out.println("Выход.");
                System.exit(0);
            }
            default -> System.out.println("Неизвестная команда.\n");
        }
    }

    private static void printUserMenu() {
        System.out.println("""
                ==== Helpdesk ====
                Вы вошли как: %s (%s)
                [1] Мои заявки
                [2] Создать заявку
                [9] Выйти из аккаунта
                [0] Выход
                """.formatted(currentUser.fio(), currentUser.role()));
        System.out.print("> ");
    }

    private static void createAnnouncementFlow() {
        System.out.println("\n--- Новая заявка ---");

        System.out.print("Категория: ");
        String category = scanner.nextLine();

        System.out.print("Заголовок: ");
        String title = scanner.nextLine();

        System.out.print("Описание: ");
        String description = scanner.nextLine();

        try {
            announcementService.createNewAnnouncement(
                    currentUser.id(),
                    category,
                    title,
                    description
            );
            System.out.println("✅ Заявка создана\n");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage() + "\n");
        } catch (RuntimeException e) {
            System.out.println("💥 Ошибка БД: " + e.getMessage() + "\n");
        }
    }

    private static void logout() {
        System.out.println("👋 До свидания, " + currentUser.fio() + "!\n");
        currentUser = null;
    }

    // ---------- Помощники ----------

    private static UserRole askRole() {
        while (true) {
            System.out.print("Роль [1 — клиент, 2 — сотрудник]: ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> { return UserRole.USER; }
                case "2" -> { return UserRole.EMPLOYEE; }
                default -> System.out.println("Введите 1 или 2.");
            }
        }
    }
}