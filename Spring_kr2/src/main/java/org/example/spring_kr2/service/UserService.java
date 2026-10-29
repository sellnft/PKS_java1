package org.example.spring_kr2.service;

import lombok.RequiredArgsConstructor;
import org.example.spring_kr2.dto.UserResponseLoginDto;
import org.example.spring_kr2.security.JwtService;
import org.springframework.security.core.userdetails.User;
import org.example.spring_kr2.exception.UserNotFoundException;
import org.example.spring_kr2.model.UserRole;
import org.example.spring_kr2.dto.UserResponseRegisterDto;
import org.example.spring_kr2.exception.UserAlreadyExistsException;
import org.example.spring_kr2.model.UserEntity;
import org.example.spring_kr2.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void validateBeforeAdd(String login, String email){
        if (userRepository.existsByLogin(login)){
            throw UserAlreadyExistsException.byLogin();
        }
        if (userRepository.existsByEmail(email)){
            throw UserAlreadyExistsException.byEmail(email);
        }
    }

    public UserDetails loadUserByUsername(String login){
        UserEntity userEntity = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не существует."));
        return User.builder()
                .username(userEntity.getLogin())
                .password(userEntity.getPasswordHash())
                .roles(userEntity.getRole().name())
                .build();
    }
    public UserResponseLoginDto loginUser(String login, String password){
        UserEntity user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("Неверный логин или пароль"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new UserNotFoundException("Неверный логин или пароль");
        }

        String token = jwtService.generateToken(user.getLogin());

        return new UserResponseLoginDto(token);
    }
    @Transactional
    public UserResponseRegisterDto registerNewUser(String login, String password, String fio, String email) {

        String hashedPassword = passwordEncoder.encode(password);
        UserEntity newUser = new UserEntity();

        newUser.setLogin(login.trim());
        newUser.setPasswordHash(hashedPassword);
        newUser.setFio(fio);
        newUser.setEmail(email.trim().toLowerCase());
        newUser.setRole(UserRole.USER);

        UserEntity savedUser = userRepository.save(newUser);
        return UserResponseRegisterDto.from(savedUser);
    }

}
