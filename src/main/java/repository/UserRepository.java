package repository;

import model.User;
import util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public void addUser(User newUser) {
        String query = "INSERT INTO users (login, fio, password_hash, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newUser.login());
            stmt.setString(2, newUser.fio());
            stmt.setString(3, newUser.passwordHash());
            stmt.setString(4, newUser.email());

            int affected = stmt.executeUpdate();

            if (affected > 0) {
                System.out.println("Пользователь успешно добавлен");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления пользователя");
        }
    }

    public boolean checkIfUserExistsByLogin(String login) {
        String query = "SELECT 1 FROM users WHERE login = ?";

        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);

            try (ResultSet resultSet = stmt.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска пользователя по телефону");
        }
    }

    public boolean checkIfUserExistsByEmail(String email) {
        String query = "SELECT 1 FROM users WHERE email = ?";

        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.SetString(1, email);

            try(ResultSet resultSet = stmt.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска пользователя по email");
        }
    }
}
