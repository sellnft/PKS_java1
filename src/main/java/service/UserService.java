package service;

import repository.JDBCUserRepository;
import model.User;
import model.UserRole;
import util.PasswordHasher;

import java.util.Optional;

public class UserService {

    private final JDBCUserRepository jdbcUserRepository;

    public UserService(JDBCUserRepository jdbcUserRepository) {
        this.jdbcUserRepository = jdbcUserRepository;
    }

    public void registerUser(String login, String password, String fio, String email, UserRole role) {

        if (jdbcUserRepository.checkIfUserExistsByLogin(login)) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует!");
        }

        if (jdbcUserRepository.checkIfUserExistsByEmail(email)) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует!");
        }

        String passwordHash = PasswordHasher.hashPassword(password);

        User user = new User(null, login, passwordHash, fio, email, role);
        jdbcUserRepository.addUser(user);
    }

    public Optional<User> loginUser(String login, String password) {
        Optional<User> usr = jdbcUserRepository.findUserByLogin(login);
        if (usr.isEmpty()) {
            return Optional.empty();
        }

        User user = usr.get();
        if (!PasswordHasher.verifyPassword(password, user.passwordHash())) {
            return Optional.empty();
        }
        return Optional.of(user);
    }
}
