package org.example.spring_kr2.service;

import lombok.RequiredArgsConstructor;
import model.User;
import model.UserRole;
import org.example.spring_kr2.exception.UserAlreadyExistsException;
import org.example.spring_kr2.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.PasswordHasher;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    @Transactional
    public UserDto registerNewUser(String login, String password, String fio, String email, UserRole role) {
        if (userRepository.existsByLogin(login)) {
            throw new UserAlreadyExistsException("Пользователь" + "с логином " + login + "уже существует");
        }

        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Пользователь" + "с email " + email + "уже существует");
        }

        UserEntity newUser =
    }

}
