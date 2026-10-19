package org.example.spring_kr2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.spring_kr2.dto.UserRequestRegisterDto;
import org.example.spring_kr2.dto.UserResponseRegisterDto;
import org.example.spring_kr2.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseRegisterDto> registerUser(@Valid @RequestBody UserRequestRegisterDto request) {
        UserResponseRegisterDto dto = userService.registerNewUser(request.login(), request.password(), request.fio(), request.email(), request.role());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    //@PostMapping("/login")
    // ResponseEntity<UserResponseLoginDto> loginUser();
}
