package service;

import repository.UserRepository;
import model.User;
import model.UserRole;

public class UserService {

    private final UserRepository userRepository = new UserRepository();

    public void registerUser(String login, String password, String email, String fio, UserRole role) {

    if (userRepository.checkIfUserExistsByLogin(login)) {
        throw new IllegalArgumentException("Пользователь с таким логином уже существует!");
    }

    if (userRepository.checkIfUserExistsByEmail(email)) {
        throw new IllegalArgumentException("Пользователь с таким email уже существует!");
    }

    }
}
