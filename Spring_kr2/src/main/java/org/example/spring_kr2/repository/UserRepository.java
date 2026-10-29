package org.example.spring_kr2.repository;

import org.example.spring_kr2.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    boolean existsByLogin(String login);

    boolean existsByEmail(String email);


    Optional<UserEntity> findByLogin(String login);

    String login(String login);
}
