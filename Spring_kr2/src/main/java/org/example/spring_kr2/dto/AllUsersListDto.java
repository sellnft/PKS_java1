package org.example.spring_kr2.dto;

import java.util.List;

public record AllUsersListDto(
        List<UserResponseForList> users
) {
}
