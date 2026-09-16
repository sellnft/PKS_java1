package service;

import repository.UserRepository;
import model.User;
import model.UserRole;
import util.PasswordHasher;

import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(String login, String password, String fio, String email, UserRole role) {

        if (userRepository.checkIfUserExistsByLogin(login)) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует!");
        }

        if (userRepository.checkIfUserExistsByEmail(email)) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует!");
        }

        String passwordHash = PasswordHasher.hashPassword(password);

        User user = new User(null, login, passwordHash, fio, email, role);
        userRepository.addUser(user);
    }

    public Optional<User> loginUser(String login, String password) {
        Optional<User> usr = userRepository.findUserByLogin(login);
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
