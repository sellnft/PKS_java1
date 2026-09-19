import model.Announcement;
import model.AnnouncementStatus;
import model.CategoryType;
import model.User;
import model.UserRole;
import repository.AnnouncementRepository;
import repository.UserRepository;
import service.AnnouncementService;
import service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = new UserService(new UserRepository());
    private static final AnnouncementService announcementService =
            new AnnouncementService(new AnnouncementRepository());

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

        if (currentUser.role() == UserRole.EMPLOYEE) {
            handleEmployeeChoice(choice);
        } else {
            handleClientChoice(choice);
        }
    }

    private static void printUserMenu() {
        if (currentUser.role() == UserRole.EMPLOYEE) {
            printEmployeeMenu();
        } else {
            printClientMenu();
        }
        System.out.print("> ");
    }

    private static void printClientMenu() {
        System.out.println("""
                ==== Helpdesk (клиент) ====
                Вы вошли как: %s
                [1] Мои заявки
                [2] Создать заявку
                [3] Все заявки
                [4] Найти заявку по ID
                [9] Выйти из аккаунта
                [0] Выход
                """.formatted(currentUser.fio()));
    }

    private static void printEmployeeMenu() {
        System.out.println("""
                ==== Helpdesk (сотрудник) ====
                Вы вошли как: %s
                [1] Свободные заявки
                [2] Все заявки
                [3] Найти заявку по ID
                [4] Взять заявку в работу
                [5] Закрыть заявку
                [9] Выйти из аккаунта
                [0] Выход
                """.formatted(currentUser.fio()));
    }

    private static void handleClientChoice(String choice) {
        switch (choice) {
            case "1" -> System.out.println("TODO: мои заявки\n");
            case "2" -> createAnnouncementFlow();
            case "3" -> showAllAnnouncementsFlow();
            case "4" -> findAnnouncementByIdFlow();
            case "9" -> logout();
            case "0" -> {
                System.out.println("Выход.");
                System.exit(0);
            }
            default -> System.out.println("Неизвестная команда.\n");
        }
    }

    private static void handleEmployeeChoice(String choice) {
        switch (choice) {
            case "1" -> showPendingAnnouncementsFlow();
            case "2" -> showAllAnnouncementsFlow();
            case "3" -> findAnnouncementByIdFlow();
            case "4" -> takeAnnouncementInWorkFlow();
            case "5" -> closeAnnouncementFlow();
            case "9" -> logout();
            case "0" -> {
                System.out.println("Выход.");
                System.exit(0);
            }
            default -> System.out.println("Неизвестная команда.\n");
        }
    }

    // ---------- Действия ----------

    private static void createAnnouncementFlow() {
        System.out.println("\n--- Новая заявка ---");

        CategoryType category = askCategory();

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

    private static void showAllAnnouncementsFlow() {
        System.out.println("\n--- Все заявки ---");

        List<Announcement> list;
        try {
            list = announcementService.getAllAnnouncements();
        } catch (RuntimeException e) {
            System.out.println("💥 Ошибка БД: " + e.getMessage() + "\n");
            return;
        }

        if (list.isEmpty()) {
            System.out.println("Заявок пока нет\n");
            return;
        }

        System.out.printf("Всего заявок: %d%n%n", list.size());
        for (Announcement a : list) {
            printAnnouncement(a);
        }
        System.out.println();
    }

    private static void showPendingAnnouncementsFlow() {
        System.out.println("\n--- Свободные заявки (в ожидании) ---");

        List<Announcement> list;
        try {
            list = announcementService.findAnnouncementsByStatus(AnnouncementStatus.PENDING);
        } catch (RuntimeException e) {
            System.out.println("💥 Ошибка БД: " + e.getMessage() + "\n");
            return;
        }

        if (list.isEmpty()) {
            System.out.println("Свободных заявок нет\n");
            return;
        }

        System.out.printf("Свободных заявок: %d%n%n", list.size());
        for (Announcement a : list) {
            printAnnouncement(a);
        }
        System.out.println();
    }

    private static void findAnnouncementByIdFlow() {
        System.out.println("\n--- Поиск заявки по ID ---");

        Integer id = askInt("ID заявки: ");
        if (id == null) {
            System.out.println("❌ Некорректный ID\n");
            return;
        }

        Optional<Announcement> found;
        try {
            found = announcementService.getAnnouncementByID(id);
        } catch (RuntimeException e) {
            System.out.println("💥 Ошибка БД: " + e.getMessage() + "\n");
            return;
        }

        if (found.isEmpty()) {
            System.out.println("❌ Заявка #" + id + " не найдена\n");
            return;
        }

        System.out.println();
        printAnnouncement(found.get());
        System.out.println();
    }

    private static void takeAnnouncementInWorkFlow() {
        System.out.println("\n--- Взять заявку в работу ---");

        Integer id = askInt("ID заявки: ");
        if (id == null) {
            System.out.println("❌ Некорректный ID\n");
            return;
        }

        boolean success;
        try {
            success = announcementService.setEmployeeForAnnouncement(id, currentUser.id());
        } catch (RuntimeException e) {
            System.out.println("💥 Ошибка БД: " + e.getMessage() + "\n");
            return;
        }

        if (success) {
            System.out.println("✅ Заявка #" + id + " взята в работу\n");
        } else {
            System.out.println("❌ Не удалось взять заявку #" + id + " в работу\n");
        }
    }

    private static void closeAnnouncementFlow() {
        System.out.println("\n--- Закрытие заявки ---");

        Integer id = askInt("ID заявки: ");
        if (id == null) {
            System.out.println("❌ Некорректный ID\n");
            return;
        }

        System.out.print("Комментарий при закрытии: ");
        String comment = scanner.nextLine();

        boolean success;
        try {
            success = announcementService.DoneAnnouncement(id, comment);
        } catch (RuntimeException e) {
            System.out.println("💥 Ошибка БД: " + e.getMessage() + "\n");
            return;
        }

        if (success) {
            System.out.println("✅ Заявка #" + id + " закрыта\n");
        } else {
            System.out.println("❌ Не удалось закрыть заявку #" + id + "\n");
        }
    }

    // ---------- Печать заявки ----------

    private static void printAnnouncement(Announcement a) {
        String assignee = (a.employeeId() != null)
                ? "пользователь #" + a.employeeId()
                : "не назначен";

        String comment = (a.comment() != null && !a.comment().isBlank())
                ? a.comment()
                : "—";

        String updatedAt = (a.updatedAt() != null)
                ? a.updatedAt().toString()
                : "—";

        System.out.printf("""
                ─────────────────────────────────────────
                #%d [%s] %s
                  Категория:   %s
                  Описание:    %s
                  Автор:       пользователь #%d
                  Ответств.:   %s
                  Создана:     %s
                  Обновлена:   %s
                  Комментарий: %s
                ─────────────────────────────────────────
                """,
                a.id(),
                a.status(),
                a.title(),
                a.category().getValueToDisplay(),
                a.description(),
                a.userId(),
                assignee,
                a.createdAt(),
                updatedAt,
                comment
        );
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

    private static CategoryType askCategory() {
        CategoryType[] values = CategoryType.values();
        while (true) {
            System.out.println("Выберите категорию:");
            for (int i = 0; i < values.length; i++) {
                System.out.printf("  [%d] %s%n", i + 1, values[i].getValueToDisplay());
            }
            System.out.print("> ");
            String choice = scanner.nextLine().trim();
            try {
                int idx = Integer.parseInt(choice) - 1;
                if (idx >= 0 && idx < values.length) {
                    return values[idx];
                }
            } catch (NumberFormatException ignored) { }
            System.out.println("Введите число от 1 до " + values.length + ".\n");
        }
    }

    private static Integer askInt(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}