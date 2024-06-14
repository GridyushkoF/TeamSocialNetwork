package ru.skillbox.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.userservice.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
