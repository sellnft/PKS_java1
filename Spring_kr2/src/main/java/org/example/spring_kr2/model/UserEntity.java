package org.example.spring_kr2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.UserRole;


@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "login")
    @NotBlank(message = "Логин не может быть пустым")
    private String login;

    @Column(name = "password_hash")
    @NotBlank(message = "Пароль не может быть пустым")
    private String passwordHash;

    @Column(name = "fio")
    @NotBlank(message = "ФИО не может быть пустым")
    private String fio;

    @Column(name = "email")
    @NotBlank(message = "Почта не может быть пустой")
    @Email(message = "Почта введена некорректно")
    private String email;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Роль не может быть пустой")
    private UserRole role;

    @Builder
    public UserEntity(String login, String passwordHash, String fio, String email, UserRole role) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.fio = fio;
        this.email = email;
        this.role = role;
    }

}
