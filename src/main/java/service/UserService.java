package service;

import repository.UserRepository;
import model.User;
import model.UserRole;

public class UserService {

    private final UserRepository userRepository = new UserRepository();

    public void registerUser(String login, String password, String email, String fio, UserRole role) {


    }
}
