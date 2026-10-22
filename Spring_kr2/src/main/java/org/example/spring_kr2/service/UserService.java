package org.example.spring_kr2.service;

import lombok.RequiredArgsConstructor;
import model.UserRole;
import org.example.spring_kr2.dto.UserResponseRegisterDto;
import org.example.spring_kr2.exception.UserAlreadyExistsException;
import org.example.spring_kr2.model.UserEntity;
import org.example.spring_kr2.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.PasswordHasher;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponseRegisterDto registerNewUser(String login, String password, String fio, String email, UserRole role) {

        String passwordHash = PasswordHasher.hashPassword(password);

        UserEntity newUser = new UserEntity();

        newUser.setLogin(login.trim());
        newUser.setPasswordHash(passwordHash);
        newUser.setFio(fio);
        newUser.setEmail(email.trim().toLowerCase());
        newUser.setRole(UserRole.USER);

        UserEntity savedUser = userRepository.save(newUser);
        return UserResponseRegisterDto.from(savedUser);
    }

}
