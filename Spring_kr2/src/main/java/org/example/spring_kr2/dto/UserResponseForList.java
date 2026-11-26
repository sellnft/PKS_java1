package org.example.spring_kr2.dto;

import org.example.spring_kr2.model.UserEntity;
import org.example.spring_kr2.model.UserRole;

public record UserResponseForList(
        Integer id,
        String login,
        String fio,
        String email,
        UserRole role
) {
    public static UserResponseForList from(UserEntity userEntity)
    {return new UserResponseForList(userEntity.getId(), userEntity.getLogin(), userEntity.getFio(), userEntity.getEmail(), userEntity.getRole());}
}
