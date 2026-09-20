package repository;

import model.User;
import model.UserRole;
import util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class JDBCUserRepository implements UserRepository{

    @Override
    public void addUser(User newUser) {
        String query = "INSERT INTO users (login, password_hash, fio, email, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newUser.login());
            stmt.setString(2, newUser.passwordHash());
            stmt.setString(3, newUser.fio());
            stmt.setString(4, newUser.email());
            stmt.setString(5, newUser.role().name());

            int affected = stmt.executeUpdate();

            if (affected > 0) {
                System.out.println("Пользователь успешно добавлен");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления пользователя: " + e);
        }
    }

    @Override
    public boolean checkIfUserExistsByLogin(String login) {
        String query = "SELECT 1 FROM users WHERE login = ?";

        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);

            try (ResultSet resultSet = stmt.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска пользователя по логину: " + e);
        }
    }

    @Override
    public boolean checkIfUserExistsByEmail(String email) {
        String query = "SELECT 1 FROM users WHERE email = ?";

        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);

            try(ResultSet resultSet = stmt.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска пользователя по email: " + e);
        }
    }

    @Override
    public Optional<User> findUserByLogin(String login) {
        String query = "SELECT id, login, password_hash, fio, email, role FROM users WHERE login = ?";

        try(Connection conn = DatabaseManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, login);

            try(ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    int userID = resultSet.getInt("id");
                    String userLogin = resultSet.getString("login");
                    String userPasswordHash = resultSet.getString("password_hash");
                    String userFio = resultSet.getString("fio");
                    String userEmail = resultSet.getString("email");
                    UserRole userRole = UserRole.valueOf(resultSet.getString("role"));
                    return Optional.of(new User(userID, userLogin, userPasswordHash, userFio, userEmail, userRole));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка поиска пользователя по логину: " + e);
        }
        return Optional.empty();
    }
}
