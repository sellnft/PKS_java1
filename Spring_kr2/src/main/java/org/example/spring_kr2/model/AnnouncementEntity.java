package org.example.spring_kr2.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import model.AnnouncementStatus;

import java.time.OffsetDateTime;

@Table(name = "announcements")
@Entity
@Getter
@Setter
public class AnnouncementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "category")
    @NotBlank(message = "Категория не может быть пустой")
    private String category;

    @Column(name = "title")
    @NotBlank(message = "Заголовок не может быть пустым")
    private String title;

    @Column(name = "description")
    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @Column(name = "status")
    @NotBlank(message = "Статус не может быть пустым")
    @Enumerated(EnumType.STRING)
    private AnnouncementStatus status;

    @Column(name = "created_at")
    @NotBlank(message = "Время создания не может быть пустым")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "comment")
    private String comment;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "user_id")
    @NotBlank(message = "Контрагент заявки не может быть пустым")
    private Integer userId;

}
