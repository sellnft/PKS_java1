package org.example.spring_kr2.dto;

import java.time.OffsetDateTime;

public record UserPasswordRemainDto(
        String login,
        OffsetDateTime timeRemain
) {}
