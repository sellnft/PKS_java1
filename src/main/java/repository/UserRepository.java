package repository;

import model.User;

import java.util.Optional;

public interface UserRepository {

    void addUser(User newUser);
    boolean checkIfUserExistsByLogin(String login);
    boolean checkIfUserExistsByEmail(String email);
    Optional<User> findUserByLogin(String login);
}
