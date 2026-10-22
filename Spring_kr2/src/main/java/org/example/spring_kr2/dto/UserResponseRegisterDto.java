package org.example.spring_kr2.dto;

import org.example.spring_kr2.model.UserRole;
import org.example.spring_kr2.model.UserEntity;

public record UserResponseRegisterDto(Integer id, String login, String fio, String email, UserRole role) {
    public static UserResponseRegisterDto from(UserEntity userEntity) {
        return new UserResponseRegisterDto(userEntity.getId(), userEntity.getLogin(), userEntity.getFio(), userEntity.getEmail(), userEntity.getRole());
    }
}
