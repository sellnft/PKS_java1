package org.example.spring_kr2.repository;

import org.example.spring_kr2.model.AnnouncementEntity;
import org.example.spring_kr2.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Integer> {
    List<AnnouncementEntity> findAllByUser(UserEntity user);
}
